package com.cscb07.taamapp;

import com.cscb07.taamapp.util.ServiceProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class ServiceProviderTest {

    private interface TestInterface {
        public int testMethod();
    }
    private class TestImplementation implements TestInterface {
        private int num;
        public TestImplementation() { num = -1; }
        public TestImplementation(int num) {
            this.num = num;
        }
        public void setNum(int num) { this.num = num; }

        @Override
        public int testMethod() {
           return num;
        }
    }

    @Test
    public void addSingleton_get2Instances_SameInstance() {
        ServiceProvider.getInstance().addSingleton(TestInterface.class, new TestImplementation(10));

        TestInterface instance1 = ServiceProvider.getInstance().getService(TestInterface.class);
        TestInterface instance2 = ServiceProvider.getInstance().getService(TestInterface.class);

        assertSame(instance1, instance2);
    }

    @Test
    public void getInstance_useImplementation_ExpectedResult() {
        ServiceProvider.getInstance().addSingleton(TestInterface.class, new TestImplementation(20));

        TestInterface instance = ServiceProvider.getInstance().getService(TestInterface.class);

        assertEquals(20, instance.testMethod());
    }

    private int testNum = 11;
    @Test
    public void addTransient_get2Instances_DifferentInstances() {
        ServiceProvider.getInstance().addTransient(TestInterface.class, () -> new TestImplementation(testNum++));

        TestInterface instance1 = ServiceProvider.getInstance().getService(TestInterface.class);
        TestInterface instance2 = ServiceProvider.getInstance().getService(TestInterface.class);

        assertNotSame(instance1, instance2);
        assertEquals(11, instance1.testMethod());
        assertEquals(12, instance2.testMethod());
    }
}
