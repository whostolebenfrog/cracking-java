package com.floatbackwards.cracking.chapter1.hashtable;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by bgriffit on 10/02/15.
 */
public class HashTableTest {

    private class ShittyHash {
        private final String value;

        public ShittyHash(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ShittyHash that = (ShittyHash) o;

            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }


    @Test
    public void canPutAndGetIntoHashTable() {
        HashTable table = new HashTable();
        Object a = new Object();
        table.put("mykey", a);
        assertThat(a, is(table.get("mykey")));
    }

    @Test
    public void canPutMultipleValuesIntoHashTable() {
        HashTable table = new HashTable();
        Object a = new Object();
        Object b = new Object();

        assertThat(a.hashCode(), is(not(b.hashCode())));

        table.put("a", a);
        table.put("b", b);

        assertThat(a, is(table.get("a")));
        assertThat(b, is(table.get("b")));

        assertThat(table.get("a"), is(not(table.get("b"))));
    }

    @Test
    public void canPutMultipleClashingValuesIntoHashTable() {
        ShittyHash a = new ShittyHash("a");
        ShittyHash b = new ShittyHash("b");

        Object oba = new Object();
        Object obb = new Object();

        HashTable table = new HashTable();
        table.put(a, oba);
        table.put(b, obb);

        assertThat(table.get(a), is(oba));
        assertThat(table.get(b), is(obb));
    }

    @Test
    public void rebalanceTest() {
        HashTable table = new HashTable();

        Object a = new Object();
        table.put("a", a);

        for (int i = 0; i < 5000; i++) {
            table.put(i, i);
        }

        assertThat(table.getHashArraySize(), is(8192));

        assertThat(a, is(table.get("a")));
    }


}
