/*
 * JLabelCImage.java
 *
 * Created on 8 juillet 2007, 10:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package cimage.observers;

import cimage.*;
import javax.swing.*;

/**
 *
 * @author HP_Propri�taire
 */
public class JLabelCImage extends JLabel implements Observer
{
    private CImage cimage;
    
    /** Creates a new instance of JLabelCImage */
    public JLabelCImage() 
    {
        super();
        setVerticalAlignment(TOP);
        setHorizontalAlignment(LEFT);

        cimage = null;
    }
    
    public JLabelCImage(CImage ci)
    {
        super();
        setVerticalAlignment(TOP);
        setHorizontalAlignment(LEFT);

        cimage = null;
        setCImage(ci);
    }
    
    public void setCImage(CImage ci)
    {
        if (cimage != null) cimage.setObserver(null);
        cimage = ci;
        cimage.setObserver(this);
        update();
    }
    
    public void update()
    {
        setIcon(new ImageIcon(cimage.getImage()));
    }
    
    public CImage getCImage()
    {
        return cimage;
    }
}
