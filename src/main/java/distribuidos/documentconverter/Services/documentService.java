package distribuidos.documentconverter.Services;

import distribuidos.documentconverter.interfaces.IdocumentService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class documentService extends UnicastRemoteObject implements IdocumentService {

    private static final Object lock = new Object();
    private static final String OFFICE_RUTA = "C:\\Program Files\\LibreOffice\\program\\soffice.exe";
    private static final String PDF_OUTPUT_DIR = "D:\\DocumentsPdfs\\";
    private final AtomicBoolean isBusy = new AtomicBoolean(false);

    public documentService() throws RemoteException {
        super();
    }

    @Override
    public List<byte[]> convertToPDF(List<String> docFilePaths) throws RemoteException {
        List<byte[]> pdfFiles = new ArrayList<>();

        for (String docFilePath : docFilePaths) {
            try {
                File docFile = new File(docFilePath);
                if (!docFile.exists()) {
                    System.out.println("El archivo no existe: " + docFilePath);
                    continue; 
                }

                String pdfFileName = docFile.getName().replaceAll("\\.\\w+$", ".pdf");
                String pdfFilePath = PDF_OUTPUT_DIR + pdfFileName;

                String[] command = {OFFICE_RUTA, "--headless", "--convert-to", "pdf", docFilePath, "--outdir", PDF_OUTPUT_DIR};

                synchronized (lock) {
                    isBusy.set(true);
                    ProcessBuilder pb = new ProcessBuilder(command);
                    Process process = pb.start();
                    int exitCode = process.waitFor();

                    if (exitCode == 0) {
                        System.out.println("PDF generado para: " + docFilePath);
                        File pdfFile = new File(pdfFilePath);
                        if (pdfFile.exists()) {
                            pdfFiles.add(Files.readAllBytes(pdfFile.toPath()));
                        } else {
                            System.out.println("No se encontró el archivo PDF generado: " + pdfFilePath);
                        }
                    } else {
                        System.out.println("Error al generar PDF: " + docFilePath);
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error procesando el archivo " + docFilePath);
                e.printStackTrace();
            } finally {
                isBusy.set(false); // Marcar como no ocupado
            }
        }

        return pdfFiles;
    }

    @Override
    public boolean isNodeAvailable() throws RemoteException {
        return !isBusy.get();
    }
}