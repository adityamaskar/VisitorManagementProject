# VistorProject

When you want to run only the visitor service with the UI and dont want to eureka to start : - 

1. Remove the eureka from the POM
2. Use the direct port on the react UI part to point this specific service.
3. Uncomment -> src/main/java/com/visitorproject/API/UserProfileAPI.java  -> @CrossOrigin(origins = "*") // if using gateway then keep commented otherwise uncomment.
4. Uncomment -> src/main/java/com/visitorproject/VisitorprojectApplication.java -> similar.
5. Uncomment -> src/main/java/com/visitorproject/API/VisitTrackerAPI.java -> similar
5. comment -. src/main/java/com/visitorproject/config/RestTemplateConfig.java


