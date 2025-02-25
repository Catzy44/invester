package me.catzy.invester;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
  public Date convertToDatabaseColumn(LocalDate locDate) {
    return (locDate == null) ? null : Date.valueOf(locDate);
  }
  
  public LocalDate convertToEntityAttribute(Date sqlDate) {
    return (sqlDate == null) ? null : sqlDate.toLocalDate();
  }
}
