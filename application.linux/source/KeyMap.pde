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

  float sine(char _letter, float increment, float amplitude) {
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
  
    float noisy(char _letter, float increment1, float increment2, float amplitude) {
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

  void makeRowQ(float _x, float _y, float _cellwidth, float _cellheight, color filldefault, color fillkey) {
    float x = _x;
    float y = _y;
    float w = _cellwidth;
    float h = _cellheight;
    color c1 = filldefault;
    color c2 = fillkey;
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

  void makeRowA(float _x, float _y, float _cellwidth, float _cellheight, color filldefault, color fillkey) {
    float x = _x;
    float y = _y;
    float w = _cellwidth;
    float h = _cellheight;
    color c1 = filldefault;
    color c2 = fillkey;
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

  void makeRowZ(float _x, float _y, float _cellwidth, float _cellheight, color filldefault, color fillkey) {
    float x = _x;
    float y = _y;
    float w = _cellwidth;
    float h = _cellheight;
    color c1 = filldefault;
    color c2 = fillkey;
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

  float updateRowQ() {
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

  float updateRowA() {
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

  float updateRowZ() {
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






