package io.praecepta.rules.engine.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import io.praecepta.core.helper.PraeceptaCollectionUtil;

public class CollectionUtilTest {

    @Test
    public void test_isEmpty(){

        List<String> list = new ArrayList<>();

        boolean status = PraeceptaCollectionUtil.isEmpty(list);

        Assert.assertTrue(status);
    }
    @Test
    public void test_isEmpty_false(){

        List<String> list = new ArrayList<>();
        list.add("test");
        boolean status = PraeceptaCollectionUtil.isEmpty(list);

        Assert.assertFalse(status);
    }

    @Test
    public void test_isNotEmpty_false(){

        List<String> list = new ArrayList<>();
        boolean status = PraeceptaCollectionUtil.isNotEmpty(list);

        Assert.assertFalse(status);
    }

    @Test
    public void test_isNotEmpty(){

        List<String> list = new ArrayList<>();
        list.add("test");
        boolean status = PraeceptaCollectionUtil.isNotEmpty(list);

        Assert.assertTrue(status);
    }

    @Test
    public void test_chunk(){

        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test1");
        list.add("test2");
        list.add("test3");
        list.add("test4");
        list.add("test5");
        list.add("test6");
        list.add("test7");
        list.add("test7");

        List<List<String>> chunkList = PraeceptaCollectionUtil.chunkListItems(list, 4);

        Assert.assertNotNull(chunkList);
        Assert.assertEquals(3, chunkList.size());
    }

    @Test
    public void test_chunkSet(){

        Set<String> list = new HashSet<>();
        list.add("test");
        list.add("test1");
        list.add("test2");
        list.add("test3");
        list.add("test4");
        list.add("test5");
        list.add("test6");
        list.add("test7");
        list.add("test7");

        Set<Set<String>> chunkList = PraeceptaCollectionUtil.chunkSetItems(list, 4);

        Assert.assertNotNull(chunkList);
        Assert.assertEquals(2, chunkList.size());
    }

}
