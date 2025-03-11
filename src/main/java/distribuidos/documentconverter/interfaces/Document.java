/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package distribuidos.documentconverter.interfaces;

import java.io.Serializable;


public class Document implements Serializable {
    private static final long serialVersionUID = 1L; 
    private String name;
    private String path;
    private byte[] content;

    public Document(String name, String path, byte[] content) {
        this.name = name;
        this.path = path;
        this.content = content;
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
}


