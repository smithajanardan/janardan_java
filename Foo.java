
public class Foo {


    
    // main is where program starts
    public static void main(String[] args) {
        if(args != null && args.length > 0)
            System.out.println(args[0]);
        
        System.out.println("Hello CS324e"); // say hello
        int x = 12;
        double y = 1.2;
        char ch = 'x';
        boolean flag = true;
        String st = "Hello 324e";
        System.out.println();
        System.out.println(x);
        System.out.println(y);
        System.out.println(flag);
        System.out.println(st);
        System.out.println(st + x + y + " " + flag + ch + st);
    }

}
