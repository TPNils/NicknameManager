package be.spyproof.nickmanager.performace;

import java.io.File;

/**
 * Created by Spyproof on 08/11/2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        String host = "localhost", database = "nickname_test", user = "root", password = "", tempDir = "E:\\tmp";
        int port = 3306;

        if (args.length == 0)
        {
            String filename = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
            System.out.println("Please give the following parameters: host, port, database name, user, password, working directory");
            System.out.println("Empty password example: java -jar " + filename + " 127.0.0.1 3306 nickname root \"\" E:\\tmp");
            System.out.println("Example 2 : java -jar " + filename + " 127.0.0.1 3306 nickname root MySuperSecretPassword E:\\tmp");
        }
        if (args.length > 0)
            host = args[0];
        if (args.length > 1)
            port = Integer.parseInt(args[1]);
        if (args.length > 2)
            database = args[2];
        if (args.length > 3)
            user = args[3];
        if (args.length > 4)
            password = args[4];
        if (args.length > 5)
            tempDir = args[5];

        TestReference.Init(host, port, database, user, password, tempDir);
        Performance performance = new Performance();
        try
        {
            performance.mySql();
            System.out.println();
            performance.gson();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
