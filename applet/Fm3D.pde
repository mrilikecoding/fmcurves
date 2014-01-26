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
  float calcX(float _increment, float _carrier, float _modulator, float _amplitude) {
    increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
    x = cos(increment*carrier) + cos(increment*modulator) * amplitude;
    return x;
  }
  
  float calcY(float _increment, float _carrier, float _modulator, float _amplitude) {
    increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
    y = sin(increment*carrier) + sin(increment*modulator) * amplitude;
    return y;
  }
  
  float calcZ(float _increment, float _carrier, float _modulator, float _amplitude) {
    increment = _increment;
    carrier = _carrier;
    modulator = _modulator;
    amplitude = _amplitude;
    z = ((sin(increment)*amplitude)) * tan((cos(modulator)*amplitude));
    return z;
  }
}
