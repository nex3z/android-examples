package com.nex3z.examples.retrofit2.rest.typeadapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public abstract class CustomTypeAdapterFactory<C> implements TypeAdapterFactory {

    private final Class<C> mTargetClass;

    public CustomTypeAdapterFactory(Class<C> customizedClass) {
        mTargetClass = customizedClass;
    }

    @Override @SuppressWarnings("unchecked")
    public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        return type.getRawType() == mTargetClass
                ? (TypeAdapter<T>) customAdapter(gson, (TypeToken<C>) type)
                : null;
    }

    private TypeAdapter<C> customAdapter(Gson gson, TypeToken<C> type) {
        final TypeAdapter<C> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return new TypeAdapter<C>() {
            @Override
            public void write(JsonWriter out, C value) throws IOException {
                JsonElement tree = delegate.toJsonTree(value);
                beforeWrite(value, tree);
                elementAdapter.write(out, tree);
            }
            @Override
            public C read(JsonReader in) throws IOException {
                JsonElement tree = elementAdapter.read(in);
                afterRead(tree);
                return delegate.fromJsonTree(tree);
            }
        };
    }

    protected void beforeWrite(C source, JsonElement tree) {}

    protected void afterRead(JsonElement tree) {}

}
