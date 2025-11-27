package com.example.teste1.view;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
public class mascaraData {
    public static TextWatcher mask(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            boolean atualizando;
            String old = "";
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", "");

                String mascara = "";

                if (atualizando) {
                    old = str;
                    atualizando = false;
                    return;
                }

                int i = 0;
                for (char m : mask.toCharArray()) {
                    if(m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e){
                        break;
                    }
                    i++;
                }
                atualizando = true;
                ediTxt.setText(mascara);

                ediTxt.setSelection(mascara.length());
            }
        };
    }
}
