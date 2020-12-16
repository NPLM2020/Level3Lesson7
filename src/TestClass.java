import annotations.Test;

public class TestClass {

    @Test(order = 1)
    public void test() {
        System.out.println("\ntest 1 has completed!");
    }

    @Test(order = 2)
    public void test2() {
        System.out.println("\ntest 2 has completed!");
    }

    @Test(order = 7)
    public void test3() {
        System.out.println("\ntest 3 has completed!");
    }

    @Test(order = 4)
    public void test4() {
        System.out.println("\ntest 4 has completed!");
    }

    @Test
    public boolean test0() {
        System.out.println("\ntest 0 has completed!");
        return true;
    }

    @Test(order = 11)
    public void test11() {
        System.out.println("\ntest 11 has completed!");
    }
}
