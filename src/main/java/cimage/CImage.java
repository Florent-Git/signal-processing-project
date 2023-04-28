/*
 * CImage.java
 *
 * Created on 6 juillet 2007, 14:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cimage;

import cimage.observers.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 *
 * @author HP_Propri�taire
 */
public abstract class CImage 
{
    protected BufferedImage image;
    protected int largeur;
    protected int hauteur;
    
    protected Graphics contexte;
    protected Observer observer;
    
    /** Creates a new instance of CImage */
    protected CImage()
    {
        largeur = 256;
        hauteur = 256;
        image = new BufferedImage(largeur,hauteur,BufferedImage.TYPE_INT_ARGB);
        contexte = image.getGraphics();
        setObserver(null);
    }
    
    public void setObserver(Observer ob)
    {
        observer = ob;
    }
    
    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
    
    public void enregistreFormatPNG(File f) throws IOException
    {
        ImageIO.write(image,"png",f);
    }
    
    public void charge(File f) throws IOException
    {
        image = ImageIO.read(f);
        largeur = image.getWidth();
        hauteur = image.getHeight();
        contexte = image.getGraphics();
        if (getObserver() != null) getObserver().update();
    }

    public Graphics getContexte() {
        return contexte;
    }

    public Observer getObserver() {
        return observer;
    }
}
