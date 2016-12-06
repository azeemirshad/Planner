package com.plan.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class ViewScannedFilesUtils 
{
	private int fileMaxSize = Integer.valueOf(Environment.getFileMaxSize());
	
	
	public ViewScannedFilesUtils() 
	{
		// TODO Auto-generated constructor stub
	}
	
	
//	Courtesy:    BalusC
	public void viewScannedFile(String contentType, String filePath, String fileName) 
			throws IOException 
	{

		System.out.println("In view scanned file utils, Appl name is "+Environment.getApplName());
        // Prepare.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        

        File file = new File(filePath, fileName);
//        File file = new File(Environment.getMedicalReportsStoragePath(), "testImage.jpg");
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            // Open file.
            input = new BufferedInputStream(new FileInputStream(file), 
            		fileMaxSize);

            // Init servlet response.
            response.reset();
//            response.setHeader("Content-Type", "image/jpeg");
//            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName+ "\"");
            output = new BufferedOutputStream(response.getOutputStream(), fileMaxSize);

            // Write file contents to response.
            byte[] buffer = new byte[fileMaxSize];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }

        // Inform JSF that it doesn't need to handle response.
        // This is very important, otherwise you will get the following exception in the logs:
        // java.lang.IllegalStateException: Cannot forward after response has been committed.
        facesContext.responseComplete();
    }

    // Helpers (can be refactored to public utility class) ----------------------------------------

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it. It may be useful to 
                // know that this will generally only be thrown when the client aborted the download.
                e.printStackTrace();
            }
        }
    }
	
	
	

}
