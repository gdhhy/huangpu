package com.xz.upload;

/**
 * Created by hhy on 17-5-23.
 */
import com.xz.upload.pojo.FileBucket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class FileValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return FileBucket.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        FileBucket file = (FileBucket) obj;

        if(file.getFile()!=null){
            if (file.getFile().getSize() == 0) {
                errors.rejectValue("file", "missing.file");
            }
        }
    }
}