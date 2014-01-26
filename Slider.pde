//this slider class creates vertical or horrizontal sliders
class Slider {
  float x, y, w, h;
  float sx, sy, sw, sh;
  color a, b;
  color c1, c2;
  color hover;
  color click;
  String info;
  
//control x and y positions, height and width, 
//background color, and slider color
  Slider(float _x, float _y, float _w, float _h, color _a, color _b) {
    x = _x;
    y = _y;
    w = _w;
    h = _h;
    a = _a;
    b = _b;
    sx = x;
    sy = y;
    hover = #777777;
    click = #dd3333;
  }

//draw a vertical slider
  void makeV() {
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
  void makeH() {
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
  void checkV() {
    if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y+h) {
      c2 = hover;
    } 
    if (mousePressed && mouseY > y && mouseY < y + h-sh && mouseX > x && mouseX < x + w) {
      c2 = click;
      sy = mouseY;
    }   
  }

//track updates for horizontal slider
  void checkH() {
    if (mouseX > x && mouseX < x + w && mouseY > y && mouseY < y+h) {
      c2 = hover;
    } 
    if (mousePressed && mouseX > x && mouseX < x + w-sw && mouseY > y && mouseY < y+h) {
      c2 = click;
      sx = mouseX;
    }
  }

//return values for vertical slider. set minimum and maximum range
  float getV(float minVal, float maxVal) {
    return map(sy, y + h-sh, y, minVal, maxVal);
  }

//return values for horrizontal slider. set minimum and maximum range
  float getH(float minVal, float maxVal) {
    return map(sx, x + w-sw, x, maxVal, minVal);
  }

void labelH(String _info){ 
  info = _info;
  pushStyle();
  fill(100, 0, 0);  
  textAlign(LEFT);
  text(info, x, y-h/4);
  popStyle();
}
  
void labelV(String _info){ 
  info = _info;
  pushStyle();
  fill(100, 0, 0);
  textAlign(CENTER);
  text(info, x+w/2, y-10);
  popStyle();
}
  
////change default colors for hovering and clicking on slider
 void slideColor(color hovering, color clicking) {
    hover = hovering;
    click = clicking;
 }
}

