package com.floatbackwards.cracking.chapter1.hashtable;

import java.util.LinkedList;

/**
 * Created by bgriffit on 10/02/15.
 */
public class HashTable {

   private static int DEFAULT_SIZE = 256;
   private static int REBALANCE_RATIO = 1 / 2;

   private int entries;
   private Object[] hashArray;
   private int hashArraySize = DEFAULT_SIZE;

   public HashTable() {
     hashArray = new Object[DEFAULT_SIZE];
   }

   private static int hashCodeToArraySize(int hashCode, int hashArraySize) {
      return hashCode % hashArraySize;
   }

   private static class HashTableEntry {
      private final Object key;
      private final Object value;

      public HashTableEntry(Object key, Object value) {
         this.key = key;
         this.value = value;
      }
   }

   private void rebalance() {
      hashArraySize *= 2;
      Object[] tmpHashArray = new Object[hashArraySize];
      for (Object node : hashArray) {
         if (node != null) {
            if (node instanceof HashTableEntry) {
               HashTableEntry entry = (HashTableEntry)node;
               put(entry.key, entry.value, tmpHashArray, hashArraySize);
            } else {
               for (Object subNode : ((LinkedList) node)) {
                  HashTableEntry subEntry = (HashTableEntry)subNode;
                  put(subEntry.key, subEntry.value, tmpHashArray, hashArraySize);
               }
            }
         }
      }

      hashArray = tmpHashArray;
   }

   public void put(Object key, Object o) {
      entries++;

      if (entries / hashArraySize > REBALANCE_RATIO) {
         rebalance();
      }

      put(key, o, hashArray, hashArraySize);
   }

   // This actually has a bug if you try and store LinkedLists
   private static void put(Object key, Object o, Object[] data, int currentArraySize) {
      int hashKey = hashCodeToArraySize(key.hashCode(), currentArraySize);
      HashTableEntry entry = new HashTableEntry(key, o);
      Object node = data[hashKey];
      if (node == null) {
         data[hashKey] = entry;
      } else if (node instanceof LinkedList) {
         ((LinkedList) node).add(entry);
      } else {
         LinkedList newNode = new LinkedList();
         newNode.add(node);
         newNode.add(entry);
         data[hashKey] = newNode;
      }
   }

   public Object get(Object key) {
      int hashKey = hashCodeToArraySize(key.hashCode(), hashArraySize);
      Object node = hashArray[hashKey];

      if (node instanceof HashTableEntry) {
         return ((HashTableEntry)node).value;
      } else if (node instanceof LinkedList) {
         for (Object listNode : (LinkedList)node) {
            HashTableEntry entry = (HashTableEntry)listNode;
            if (entry.key.equals(key)) {
               return entry.value;
            }
         }
         return null;
      }
      return null;
   }

   public int getHashArraySize() {
      return hashArraySize;
   }

}
