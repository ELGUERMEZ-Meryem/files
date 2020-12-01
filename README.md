# files
Spring provides a MultipartFile interface to handle HTTP multi-part requests for uploading files. Multipart-file requests break large files into smaller chunks which makes it efficient for file uploads.


This application is for File upload in that user can:

- see the upload process (percentage)
- view all uploaded files
- upload one or many files

1. application.properties:

– spring.servlet.multipart.max-file-size: max file size for each request.
– spring.servlet.multipart.max-request-size: max request size for a multipart/form-data.

# Save files in a static directory



# Save files in the database:

# Save files in the:

# Resources:
https://bezkoder.com/spring-boot-upload-file-database/

https://bezkoder.com/angular-spring-boot-file-upload/
