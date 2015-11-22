package org.nhnnext.nearhoneytip.remote;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class LenientGsonConverter extends GsonConverter {
    private Gson gson;

    public LenientGsonConverter(Gson gson) {
        super(gson);
        this.gson = gson;
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        boolean willCloseStream = false;
        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(body.in()));
            jsonReader.setLenient(true);

            Object o = gson.fromJson(jsonReader, type);
            willCloseStream = true;
            return o;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (willCloseStream) {
                closeStream(body);
            }
        }

//        String dirty = body.toString();
//        String clean = dirty.replaceAll("(^\\(|\\)$)", "");
//        body = new JsonTypedInput(clean.getBytes(Charset.forName("UTF-8")));

        return super.fromBody(body, type);
    }

    private void closeStream(TypedInput body) {
        try {
            InputStream in = body.in();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class JsonTypedInput implements TypedInput{

        private final byte[] mStringBytes;

        JsonTypedInput(byte[] stringBytes) {
            this.mStringBytes = stringBytes;
        }


        @Override
        public String mimeType() {
            return "application/json; charset=UTF-8";
        }



        @Override
        public long length() {
            return mStringBytes.length;
        }

        @Override
        public InputStream in() throws IOException {
            return new ByteArrayInputStream(mStringBytes);
        }
    }
}
