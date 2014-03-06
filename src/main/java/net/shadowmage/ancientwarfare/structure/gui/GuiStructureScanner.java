package net.shadowmage.ancientwarfare.structure.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.shadowmage.ancientwarfare.core.container.ContainerBase;
import net.shadowmage.ancientwarfare.core.gui.GuiContainerBase;
import net.shadowmage.ancientwarfare.core.gui.Listener;
import net.shadowmage.ancientwarfare.core.gui.GuiContainerBase.ActivationEvent;
import net.shadowmage.ancientwarfare.core.gui.elements.Button;
import net.shadowmage.ancientwarfare.core.gui.elements.Checkbox;
import net.shadowmage.ancientwarfare.core.gui.elements.GuiElement;
import net.shadowmage.ancientwarfare.core.gui.elements.Label;
import net.shadowmage.ancientwarfare.core.gui.elements.Text;
import net.shadowmage.ancientwarfare.core.network.NetworkHandler;
import net.shadowmage.ancientwarfare.core.network.PacketGui;
import net.shadowmage.ancientwarfare.structure.network.PacketStructure;
import net.shadowmage.ancientwarfare.structure.template.build.validation.StructureValidationType;
import net.shadowmage.ancientwarfare.structure.template.build.validation.StructureValidator;

public class GuiStructureScanner extends GuiContainerBase
{

private Text nameInput;
private Label validationTypeLabel;
private Checkbox includeOnExport;

protected StructureValidationType validationType = StructureValidationType.GROUND;
protected StructureValidator validator;

public GuiStructureScanner(ContainerBase par1Container)
  {
  super(par1Container, 256, 240, defaultBackground);
  validator = validationType.getValidator();
  this.shouldCloseOnVanillaKeys = false;
  }

@Override
public void initElements()
  {
  Label label = new Label(8, 8, "Input Name:");
  this.addGuiElement(label);
  
  nameInput = new Text(8, 8+12, 160, "", this);
  this.addGuiElement(nameInput);
  
  Button button = new Button(256-55-8, 8, 55, 16, "Export");
  button.addNewListener(new Listener(Listener.MOUSE_UP)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if(widget.isMouseOverElement(evt.mx, evt.my))
        {
        export();
        }
      return true;
      }
    });
  this.addGuiElement(button);
  
  button = new Button(256-55-8, 8+16, 55, 16, "Cancel");
  button.addNewListener(new Listener(Listener.MOUSE_UP)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if(widget.isMouseOverElement(evt.mx, evt.my))
        {
        Minecraft.getMinecraft().displayGuiScreen(null);
        }
      return true;
      }
    });
  this.addGuiElement(button);
  
  int totalHeight = 36;
  
  Checkbox box = new Checkbox(8, totalHeight, 16, 16, "Include in game Immediately?");
  box.setChecked(true);
  this.addGuiElement(box);
  includeOnExport = box;
  totalHeight+=16+8;
  
  validationTypeLabel = new Label(8, totalHeight, "Current validation type: "+validationType.getName()); 
  this.addGuiElement(validationTypeLabel);
  totalHeight+=10;
  
  button = new Button(8, totalHeight, 120, 16, "Setup Validation");
  button.addNewListener(new Listener(Listener.MOUSE_UP)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if(widget.isMouseOverElement(evt.mx, evt.my))
        {
        Minecraft.getMinecraft().displayGuiScreen(new GuiStructureValidationSettings(GuiStructureScanner.this));        
        }
      return true;
      }
    });
  this.addGuiElement(button);
  totalHeight+=16;
  
  button = new Button(8, totalHeight, 120, 16, "Select Biomes");
  button.addNewListener(new Listener(Listener.MOUSE_UP)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if(widget.isMouseOverElement(evt.mx, evt.my))
        {
        Minecraft.getMinecraft().displayGuiScreen(new GuiStructureBiomeSelection(GuiStructureScanner.this));        
        }
      return true;
      }
    });
  this.addGuiElement(button);
  totalHeight+=16;
  
  button = new Button(8, totalHeight, 120, 16, "Select Targets");
  button.addNewListener(new Listener(Listener.MOUSE_UP)
    {
    @Override
    public boolean onEvent(GuiElement widget, ActivationEvent evt)
      {
      if(widget.isMouseOverElement(evt.mx, evt.my))
        {
        Minecraft.getMinecraft().displayGuiScreen(new GuiStructureBlockSelection(GuiStructureScanner.this));        
        }
      return true;
      }
    });
  this.addGuiElement(button);
  totalHeight+=16;
  
  button = new Button(8, totalHeight, 120, 16, "Select Dimensions");
  this.addGuiElement(button);
  totalHeight+=16;  
  }

@Override
public void setupElements()
  {
  validationTypeLabel.setText("Current validation type: "+validationType.getName());
  }

private void export()
  {
  String name = nameInput.getText();
  if(!validateName(name))
    {
    Minecraft.getMinecraft().displayGuiScreen(new GuiStructureIncorrectName(this));    
    }
  else
    {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("name", name);
    tag.setBoolean("export", includeOnExport.checked());
    NBTTagCompound val = new NBTTagCompound();
    validator.writeToNBT(val);
    tag.setTag("validation", val);
    
    PacketGui pkt = new PacketGui();
    pkt.packetData = tag;
    NetworkHandler.sendToServer(pkt);
    }
  }

private boolean validateName(String name)
  {
  if(name.equals("")){return false;}
  boolean valid = true;
  for(int i = 0; i < name.length(); i++)
    {
    if(!validateChar(name.charAt(i)))
      {
      return false;
      }
    }
  return true;
  }

private boolean validateChar(char ch)
  {
  return false;
  }

}
