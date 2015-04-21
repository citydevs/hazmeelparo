package com.citydevs.hazmeelparo.config;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.citydevs.hazmeelparo.R;
import com.citydevs.hazmeelparo.utils.Utils;

/**
 * Created by mikesaurio on 20/04/15.
 */
public class Configuracion extends View {

    private Activity context;
    private View view;

    public Configuracion(Activity context) {
        super(context);
        this.context = context;
        init();
    }

    public Configuracion(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public Configuracion(Activity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.config_activity, null);


        Switch mySwitch_CAS = (Switch) view.findViewById(R.id.switch_cas);
        mySwitch_CAS.setChecked(Boolean.parseBoolean(new Utils(context).getPreferenciasCAS()));
        mySwitch_CAS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                new Utils(context).setPreferenciasCAS(isChecked+"");
            }
        });

        Switch mySwitch_Mensaje = (Switch) view.findViewById(R.id.switch_contacto);
        mySwitch_Mensaje.setChecked(Boolean.parseBoolean(new Utils(context).getPreferenciasMensaje()));
        mySwitch_Mensaje.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                new Utils(context).setPreferenciasMensaje(isChecked+"");
            }
        });

        Switch mySwitch_Mando = (Switch) view.findViewById(R.id.switch_mando);
        mySwitch_Mando.setChecked(Boolean.parseBoolean(new Utils(context).getPreferenciasContactarMando()));
        mySwitch_Mando.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                new Utils(context).setPreferenciasContactarMando(isChecked+"");
            }
        });


    }

public View getView(){
    return view;
}
}
