package cz.mendelu.devtrendsexplorer.utils.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public class ObjectResponse<T> {

    T content;

    // factory method
    static public <I, T> ObjectResponse<T> of(I obj, Function<I, T> mapper) {
        int version = 1; // example of meta information
        return new ObjectResponse<>(mapper.apply(obj), version);
    }

    // Here is place for meta information about content:

    int version; // example of meta information

}
