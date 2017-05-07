package values.WritableValues;


// Defines an interface that allows us to write to a stream
// Used for writing to a file.
public interface Writable {

    // Write to the stream.
    void write(String content);

}
