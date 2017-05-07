package values.WritableValues;

public class std_out implements Writable {
    @Override
    public void write(String content) {
        System.out.print(content);
    }
}
