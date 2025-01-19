package com.fvd.hello.camel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@ApplicationScoped
@Named
@Slf4j
@SuppressWarnings("rawtypes")
public class FileFilter implements GenericFileFilter {

  @ConfigProperty(name = "filter.lastmodified.seconds.beforeintegrate")
  Integer secondsSinceLastModified;

  @Override
  public boolean accept(GenericFile file) {
    LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.getLastModified()), TimeZone.getDefault().toZoneId());
    log.debug("File : {}. Last modified timestamp : {}.", file.getFileName(), lastModified);
    return !lastModified.isAfter(LocalDateTime.now().minusSeconds(secondsSinceLastModified));
  }

}
