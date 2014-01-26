import processing.core.*; 
import processing.xml.*; 

import beads.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class FM_Curves extends PApplet {

//FM Curves takes user input with a simple FM patch 
//and manipulates a series of Bezier curves with 
//trig functions.

//declare audio elements

AudioContext ac; 
float gainset = 0.3f;
Gain g = new Gain(ac, 1, gainset);

WavePlayer modulator; 
Glide modFreq; 
WavePlayer carrier; 

KeyMap keys;

//declare sliders
Slider carslide, modslide, pointslide, volumeslide, scaleslide;
String carinfo, modinfo, pointinfo;

//declare font
PFont helvetica;

//initial variables
int pointnum = 250;
float rad = 50;
float cfreq = 220.0f;
float mfreq = 1.0f;

//declare signal visualizer and initialize
Fm3D visualizer;

//slider colors
int slidebg;
int slide;

//incrementers
float a = 0;
float b = 0;

//x, y, z coordinate arrays
float[] x = new float[pointnum];
float[] y = new float[pointnum];
float[] z = new float[pointnum];

//bezier intermediate points
float x1, y1, z1;

public void setup() {
  size(640, 480, P3D);
  colorMode(HSB, 100);
  
  //initialize keymap
  keys = new KeyMap();
  
  // initialize beads audio parameters
  ac = new AudioContext(); 
  modFreq = new Glide(ac, 20, 30); 
  modulator = new WavePlayer(ac, modFreq, Buffer.SINE); 
  Function frequencyModulation = new Function(modulator) { 
    public float calculate() { 
      //link carrier frequency variable to audio generator (carrier freq is the offset)
      return (x[0] * 200.0f) + cfreq;
    }
  };
  carrier = new WavePlayer(ac, frequencyModulation, Buffer.SINE); 
  
  // set gain level to a gainset variable controled by the volume slider
  g = new Gain(ac, 1, gainset);
  g.addInput(carrier); 
  ac.out.addInput(g); 
  ac.start();

  //initializing frequency modulation visualizer
  // params: (increment, carrier frequency, modulation frequency, amplitude/radius)
  visualizer = new Fm3D(a, cfreq, mfreq, rad);

  //font
  helvetica = loadFont("helvetica.vlw");

  //set slider colors
  slide = 0xffBED8BF;
  slidebg = 0xff3D463D;
  
  //x, y, w, h, bgcolor, slider color
  carslide = new Slider(25, 35, 90, 25, slidebg, slide);
  modslide = new Slider(25, 85, 90, 25, slidebg, slide);
  pointslide = new Slider(25, 135, 90, 25, slidebg, slide);
  volumeslide = new Slider(width - 50, 35, 25, 100, slidebg, slide);
  scaleslide = new Slider(width - 50, 200, 25, 100, slidebg, slide); 
}

public void draw() {
  background(100, 0, 95);
  lights();
  //change volume with slider using gainset variable
  g.setGain(gainset);
  //link audio generator to mfreq variable  
  modFreq.setValue(mfreq);
  
  //display data
  carinfo = "carrier frequency: " + nf(cfreq, 1, 2);
  modinfo = "modulation frequency: " + nf(mfreq, 1, 2);
  pointinfo = "curves: " + pointnum;
  pushStyle();
  textFont(helvetica);
  textSize(12);
  fill(100, 0, 0);

  carslide.makeH();
  carslide.checkH();
  carslide.labelH(carinfo);

  modslide.makeH();
  modslide.checkH();
  modslide.labelH(modinfo);

  pointslide.makeH();
  pointslide.checkH();
  pointslide.labelH(pointinfo);
  
  volumeslide.makeV();
  volumeslide.checkV();
  volumeslide.labelV("vol:");
  
  scaleslide.makeV();
  scaleslide.checkV();
  scaleslide.labelV("scale:");
  
  popStyle();

  //line slider values to frequencies and point number
  //set range
  cfreq = carslide.getH(220, 2200);
  mfreq = modslide.getH(1, 1000);
  pointnum = PApplet.parseInt(pointslide.getH(2, 250));
  gainset = volumeslide.getV(0, 0.5f);
  float scaleval = scaleslide.getV(0, 2);
  
  //map keyboard keys to events
  float keyScaleA = map(keys.updateRowA(), 1, 9, 1, 3);
  float keyScaleZ = map(keys.updateRowZ(), 1, 7, 1, 10);
  if(keyPressed){
  cfreq = cfreq*keyScaleA;
  mfreq = mfreq*keyScaleZ;
    switch(key){
    case 'q': mfreq = keys.sine('q', b, mfreq/4) + mfreq;
    break;
    case 'w': cfreq = keys.sine('w', a, cfreq/10) + cfreq;
    break;
    case 'e': mfreq = keys.noisy('e', a, b, 25) + mfreq;
    break;
    case 'r': cfreq = keys.noisy('r', a, b, 25) + cfreq;
    break;
    case 't': cfreq = 220;
    break;
    case 'y': cfreq = 440;
    break;
    case 'u': cfreq = 660;
    break;
    case 'i': cfreq = 880;
    break;
    case 'o': cfreq = keys.noisy('o', a, b, cfreq);
    break;
    case 'p': mfreq = keys.noisy('p', a, b, mfreq);
    }
   a+= 0.05f;
   b+= 0.03f;
  }
  //display keyboard viewer
  keys.makeRowQ(50, height-25, 15, 15, slidebg, slide);
  keys.makeRowA(215, height-25, 15, 15, slidebg, slide);
  keys.makeRowZ(365, height-25, 15, 15, slidebg, slide);
    
//set point coordinates
  for (int i = 0; i < pointnum; i++) {
      x[i] = visualizer.calcX(i, cfreq, mfreq, rad);
      y[i] = visualizer.calcY(i, cfreq, mfreq, rad);
      z[i] = visualizer.calcZ(i, cfreq, mfreq, rad);   
  }

  pushMatrix();
  translate(width/2, height/2);
    scale(scaleval);
  rotateY(radians(frameCount));

//draw 3D wave point sphere
  //alter colors based on input
 float amp = map(cfreq, 220, 1200, -2, 2);
 float hueMap = map(mfreq, 1, 1000, 0, 100);
 float satMap = map(cfreq, 200, 1200, 50, 100);
 float briMap = map(pointnum, 0, pointnum, 80, 100);
 fill(hueMap, satMap, briMap, 75);
 noStroke();
  for (int i = 0; i < pointnum; i++ ) {
    bezier(x1, y1, z1, 
    amp*x1*cos(i), y1*sin(i), amp*z1*cos(i),
    x1*sin(i), amp*y1*cos(i), z1*sin(i), 
    x[i], y[i], z[i]);
    
    x1 = visualizer.calcX(i, cfreq, mfreq, rad);
    y1 = visualizer.calcY(i, cfreq, mfreq, rad);
    z1 = visualizer.calcZ(i, cfreq, mfreq, rad);
  }
  popMatrix();
}
class Fm3D {
  float increment, carrier, modulator, amplitude;
  float x, y, z;
  
 Fm3D(float _increment, float _carrier, float _modulator, float _amplitude) {
   increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
  }

// must include a changing value to produce a continuous signal  
  public float calcX(float _increment, float _carrier, float _modulator, float _amplitude) {
    increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
    x = cos(increment*carrier) + cos(increment*modulator) * amplitude;
    return x;
  }
  
  public float calcY(float _increment, float _carrier, float _modulator, float _amplitude) {
    increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
    y = sin(increment*carrier) + sin(increment*modulator) * amplitude;
    return y;
  }
  
  public float calcZ(float _increment, float _carrier, float _modulator, float _amplitude) {
    increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
    z = ((sin(increment)*amplitude)) * tan((cos(modulator)*amplitude));
    return z;
  }
}
class KeyMap {

  char[] qrow = {
    'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'
  };
  char[] arow = {
    'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'
  };
  char[] zrow = {
    'z', 'x', 'c', 'v', 'b', 'n', 'm'
  };

  KeyMap() {
  }

  public float sine(char _letter, float increment, float amplitude) {
    char letter = _letter;
    float value = 1;
    if (keyPressed) {
      if (key == letter) {
        value = sin(increment)*amplitude;
      } 
      else { 
        value = 1;
      }
    } 
    return value;
  }
  
    public float noisy(char _letter, float increment1, float increment2, float amplitude) {
    char letter = _letter;
    float value = 1;
    if (keyPressed) {
      if (key == letter) {
        value = noise(increment1, increment2)*amplitude;
      } 
      else { 
        value = 1;
      }
    } 
    return value;
  }

  public void makeRowQ(float _x, float _y, float _cellwidth, float _cellheight, int filldefault, int fillkey) {
    float x = _x;
    float y = _y;
    float w = _cellwidth;
    float h = _cellheight;
    int c1 = filldefault;
    int c2 = fillkey;
    fill(filldefault);
    textAlign(CENTER);

    for (int i = 0; i < qrow.length; i++) { 
      fill(c1);
      text(qrow[i], x+w*i+w/2, y-5);
      if (keyPressed && key == qrow[i]) {
        fill(c2);
      } 
      else {
        fill(c1);
      }
      rect(x + w*i, y, w, h);
    }
  }

  public void makeRowA(float _x, float _y, float _cellwidth, float _cellheight, int filldefault, int fillkey) {
    float x = _x;
    float y = _y;
    float w = _cellwidth;
    float h = _cellheight;
    int c1 = filldefault;
    int c2 = fillkey;
    fill(filldefault);
    textAlign(CENTER);

    for (int i = 0; i < arow.length; i++) { 
      fill(c1);
      text(arow[i], x+w*i+w/2, y-5);
      if (keyPressed && key == arow[i]) {
        fill(c2);
      } 
      else {
        fill(c1);
      }
      rect(x + w*i, y, w, h);
    }
  }

  public void makeRowZ(float _x, float _y, float _cellwidth, float _cellheight, int filldefault, int fillkey) {
    float x = _x;
    float y = _y;
    float w = _cellwidth;
    float h = _cellheight;
    int c1 = filldefault;
    int c2 = fillkey;
    fill(filldefault);
    textAlign(CENTER);
    for (int i = 0; i < zrow.length; i++) {
      fill(c1);
      text(zrow[i], x+w*i+w/2, y-5);
      if (keyPressed && key == zrow[i]) {
        fill(c2);
      } 
      else {
        fill(c1);
      }
      rect(x+w*i, y, w, h);
    }
  }

  public float updateRowQ() {
    float value = 1;
    char letter;

    if (keyPressed) {
      letter = key;
      for (int i = 0; i < qrow.length; i++) {
        if (letter == qrow[i]) {
          value = i;
        }
      }
    }
    return value;
  }

  public float updateRowA() {
    float value = 1;
    char letter;

    if (keyPressed) {
      letter = key;
      for (int i = 0; i < arow.length; i++) {
        if (letter == arow[i]) {
          value = i;
        }
      }
    }
    return value;
  }

  public float updateRowZ() {
    float value = 1;
    char letter;

    if (keyPressed) {
      letter = key;
      for (int i = 0; i < zrow.length; i++) {
        if (letter == zrow[i]) {
          value = i;
        }
      }
    }
    return value;
  }
}






//this slider class creates vertical or horrizontal sliders
class Slider {
  float x, y, w, h;
  float sx, sy, sw, sh;
  int a, b;
  int c1, c2;
  int hover;
  int click;
  String info;
  
//control x and y positions, height and width, 
//background color, and slider color
  Slider(float _x, float _y, float _w, float _h, int _a, int _b) {
    x = _x;
    y = _y;
    w = _w;
    h = _h;
    a = _a;
    b = _b;
    sx = x;
    sy = y;
    hover = 0xff777777;
    click = 0xffdd3333;
  }

//draw a vertical slider
  public void makeV() {
    sw = w;
    sh = h/10;
    noStroke();
    fill(c1);
    rect(x, y, w, h);
    fill(c2);
    rect(sx, sy, sw, sh);
    c1 = a;
    c2 = b;
  }

//draw a horizontal slider
  public void makeH() {
    sw = w/10;
    sh = h;
    noStroke();
    fill(c1);
    rect(x, y, w, h);
    fill(c2);
    rect(sx, sy, sw, sh);
    c1 = a;
    c2 = b;
  }

//track updates for vertical slider
  public void checkV() {
    if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y+h) {
      c2 = hover;
    } 
    if (mousePressed && mouseY > y && mouseY < y + h-sh && mouseX > x && mouseX < x + w) {
      c2 = click;
      sy = mouseY;
    }   
  }

//track updates for horizontal slider
  public void checkH() {
    if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y+h) {
      c2 = hover;
    } 
    if (mousePressed && mouseX > x && mouseX < x + w-sw && mouseY > y && mouseY < y+h) {
      c2 = click;
      sx = mouseX;
    }
  }

//return values for vertical slider. set minimum and maximum range
  public float getV(float minVal, float maxVal) {
    return map(sy, y + h-sh, y, minVal, maxVal);
  }

//return values for horrizontal slider. set minimum and maximum range
  public float getH(float minVal, float maxVal) {
    return map(sx, x + w-sw, x, maxVal, minVal);
  }

public void labelH(String _info){ 
  info = _info;
  pushStyle();
  fill(100, 0, 0);  
  textAlign(LEFT);
  text(info, x, y-h/4);
  popStyle();
}
  
public void labelV(String _info){ 
  info = _info;
  pushStyle();
  fill(100, 0, 0);
  textAlign(CENTER);
  text(info, x+w/2, y-10);
  popStyle();
}
  
////change default colors for hovering and clicking on slider
 public void slideColor(int hovering, int clicking) {
    hover = hovering;
    click = clicking;
 }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "FM_Curves" });
  }
}
