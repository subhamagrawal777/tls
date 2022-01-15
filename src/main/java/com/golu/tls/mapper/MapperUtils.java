package com.golu.tls.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golu.tls.exception.AppException;
import com.golu.tls.exception.CoreErrorCode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@UtilityClass
public class MapperUtils {

    private static Map<MapperType, ObjectMapper> objectMapperMap;

    public static ObjectMapper getMapper(MapperType mapperType) {
        Preconditions.checkState(objectMapperMap != null && objectMapperMap.containsKey(mapperType), "init not called on MapperUtils");
        return objectMapperMap.get(mapperType);
    }

    public static void setup(Map<MapperType, ObjectMapper> objectMapperMap) {
        MapperUtils.objectMapperMap = objectMapperMap;
    }

    @SneakyThrows
    public static String serialize(MapperType mapperType, Object obj) {
        return getMapper(mapperType).writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T convertValue(MapperType mapperType, Object json, Class<T> clazz) {
        try {
            return getMapper(mapperType).convertValue(json, clazz);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T convertValue(MapperType mapperType, Object json, TypeReference<T> typeReference) {
        try {
            return getMapper(mapperType).convertValue(json, typeReference);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T deserialize(MapperType mapperType, byte[] bytes, Class<T> clazz) {
        try {
            return getMapper(mapperType).readValue(bytes, clazz);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T deserialize(MapperType mapperType, InputStream inputStream, Class<T> clazz) {
        if (inputStream == null) {
            return null;
        }
        try {
            return getMapper(mapperType).readValue(inputStream, clazz);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T deserialize(MapperType mapperType, InputStream inputStream, TypeReference<T> typeReference) {
        if (inputStream == null) {
            return null;
        }
        try {
            return getMapper(mapperType).readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T deserialize(MapperType mapperType, String input, Class<T> clazz) {
        if (Strings.isNullOrEmpty(input)) {
            return null;
        }
        try {
            return getMapper(mapperType).readValue(input,clazz);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T deserialize(MapperType mapperType, String input, TypeReference<T> typeReference) {
        if (input == null) {
            return null;
        }
        try {
            return getMapper(mapperType).readValue(input, typeReference);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static byte[] convertToBytes(MapperType mapperType, Object object) {
        try {
            return getMapper(mapperType).writeValueAsBytes(object);
        } catch (IOException e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    @SneakyThrows
    public static <T> T deserialize(MapperType mapperType, byte[] bytes, Class<T> clazz, T defaultValue) {
        if (bytes == null) {
            return defaultValue;
        }
        try {
            return getMapper(mapperType).readValue(bytes, clazz);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    public static <T> T deserialize(MapperType mapperType, byte[] data, TypeReference<T> typeReference) {
        if (data == null) {
            return null;
        }
        try {
            return getMapper(mapperType).readValue(data, typeReference);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    public static Map<String, Object> convertToMap(MapperType mapperType, Object source) {
        if (source == null) {
            return null;
        }
        try {
            return getMapper(mapperType).convertValue(source, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }

    public static <T> T convert(MapperType mapperType, Object source, TypeReference<T> typeReference) {
        if (source == null) {
            return null;
        }
        try {
            return getMapper(mapperType).convertValue(source, typeReference);
        } catch (Exception e) {
            throw AppException.propagate(e, CoreErrorCode.JSON_ERROR);
        }
    }
}
