package com.example.plantify.Helpers;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.iptc.IptcReader;

import java.util.Arrays;

public class File  {
   public  String getFileData(String path){
       try {
           Iterable<JpegSegmentMetadataReader> readers = Arrays.asList(new ExifReader(), new IptcReader());
           com.drew.metadata.Metadata metadata = JpegMetadataReader.readMetadata(new java.io.File(path), readers);
           for (Directory directory : metadata.getDirectories()) {
               for (Tag tag : directory.getTags()) {
                   if(tag.getTagName().equals("File Modified Date"))
                   {
                       return tag.getDescription();
                   }

               }
           }
       } catch (ImageProcessingException e) {
           throw new RuntimeException(e);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       return path;
   }
}
