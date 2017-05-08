package values.WritableValues;
import parser.ParseException;
import values.Value;
import values.ValueAbstract;

import java.io.*;


public class ValueFile extends ValueAbstract implements Writable {

    private File mfile;


    public ValueFile(String filepath) throws ParseException {
        mfile = new File(filepath);

        try {
            if (!mfile.exists() && !mfile.createNewFile()) {
                throw new ParseException("Cannot find or create a new file");
            }
        } catch (IOException e) {
            throw new ParseException("Cannot find or create a new file");
        }
    }


    @Override
    public String getName() {
        return "File";
    }

    @Override
    public int compare(Value v) {
        return 0;
    }


    public String toString() {
        return stringValue();
    }

    @Override
    public String stringValue() {
        String res = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(mfile));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            res = stringBuilder.toString();
            reader.close();
        } catch (IOException e) {
            System.out.println("Cannot read string from the file");
        }

        return res;

    }

    @Override
    public void write(String content) {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(  new FileWriter(mfile, true));
            PrintWriter out = new PrintWriter(writer);
            out.print(content);
        }
        catch ( IOException e)
        {
            System.out.println("Cannot read string from the file");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if ( writer != null) {
                    writer.close();
                }
            }
            catch ( IOException e)
            {
            }
        }
    }
}
