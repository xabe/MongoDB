package com.xabe.mongodb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bson.LazyBSONCallback;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.OutputBuffer;
import org.jongo.marshall.jackson.bson4jackson.MongoBsonFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.mongodb.BasicDBObject;
import com.mongodb.DBEncoder;
import com.mongodb.DBObject;
import com.mongodb.DefaultDBEncoder;
import com.mongodb.LazyWriteableDBObject;

public class JsonUtils {

	private final static ObjectMapper mapper = new ObjectMapper(MongoBsonFactory.createFactory());

    static {
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    public static DBObject getDbObject(Object obj) throws IOException {
    	if(null == obj)
    	{
    		throw new NullPointerException("El objecto no puede ser nulo");
    	}
        ObjectWriter writer = mapper.writer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        writer.writeValue(baos, obj);
        DBObject dbo = new LazyWriteableDBObject(baos.toByteArray(), new LazyBSONCallback());
        //turn it into a proper DBObject otherwise it can't be edited.
        DBObject result = new BasicDBObject();
        result.putAll(dbo);
        return result;
    }

    public static <T> T getPojo(DBObject obj, Class<T> clazz) throws IOException {
    	if(null == obj)
    	{
    		throw new NullPointerException("El objecto no puede ser nulo");
    	}
        ObjectReader reader = mapper.reader(clazz);
        DBEncoder dbEncoder = DefaultDBEncoder.FACTORY.create();
        OutputBuffer buffer = new BasicOutputBuffer();
        dbEncoder.writeObject(buffer, obj);

        T pojo = reader.readValue(buffer.toByteArray());

        return pojo;
    }
}
