package com.example.praga.opencv.Zouhir;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.praga.opencv.R;

public class Slider_Adapter extends PagerAdapter {


    Context mycContext;
    LayoutInflater mylLayoutInflater;

    public Slider_Adapter(Context applicationContext) {

        this.mycContext = applicationContext;

    }


    // Arrays of data
    public  String [] tutorials = {
            " 1-- Lorem ipsum dolor sit amet, sea omnium corrumpit cu, falli scripta cotidieque sit et. Cum ne ferri melius persius, an vis quas dicant evertitur. Vix possim docendi legendos ei. Modo nemore ex vel, id vix nonumes signiferumque, id constituto comprehensam his.\n" +
                    "    Ludus legimus at pri, mei iisque civibus consequuntur ut. Ea per nulla persecuti. Duis porro adipiscing ius ne, id sit iudico cetero omittantur. Modo esse paulo vis ut, quaeque maluisset molestiae no ius, ea latine quaerendum persequeris eos. Purto tritani neglegentur at quo.\n" +
                    "    Scripta insolens pertinax cu vis. Eu vel dicta fastidii. Agam cibo utamur et usu, sed probo dicit te, an sit sale munere. Vim autem tritani volutpat an, cu tibique apeirian abhorreant sea. No veritus suscipit nam, id mei utamur legendos forensibus. Viderer diceret eos cu, legendos corrumpit disputando vim et, ullum primis eam id.\n" +
                    "    Sed ea malis nostrud prodesset, per te dolor utamur hendrerit. Solet ridens tamquam his te. An liber bonorum definiebas sea. Civibus omittam contentiones cu quo, hinc euismod omnesque pri eu, exerci delicata ei cum."
            ,
            " 2-- Lorem ipsum dolor sit amet, sea omnium corrumpit cu, falli scripta cotidieque sit et. Cum ne ferri melius persius, an vis quas dicant evertitur. Vix possim docendi legendos ei. Modo nemore ex vel, id vix nonumes signiferumque, id constituto comprehensam his.\n" +
                    "    Ludus legimus at pri, mei iisque civibus consequuntur ut. Ea per nulla persecuti. Duis porro adipiscing ius ne, id sit iudico cetero omittantur. Modo esse paulo vis ut, quaeque maluisset molestiae no ius, ea latine quaerendum persequeris eos. Purto tritani neglegentur at quo.\n" +
                    "    Scripta insolens pertinax cu vis. Eu vel dicta fastidii. Agam cibo utamur et usu, sed probo dicit te, an sit sale munere. Vim autem tritani volutpat an, cu tibique apeirian abhorreant sea. No veritus suscipit nam, id mei utamur legendos forensibus. Viderer diceret eos cu, legendos corrumpit disputando vim et, ullum primis eam id.\n" +
                    "    Sed ea malis nostrud prodesset, per te dolor utamur hendrerit. Solet ridens tamquam his te. An liber bonorum definiebas sea. Civibus omittam contentiones cu quo, hinc euismod omnesque pri eu, exerci delicata ei cum."
            ,
            " 3-- Lorem ipsum dolor sit amet, sea omnium corrumpit cu, falli scripta cotidieque sit et. Cum ne ferri melius persius, an vis quas dicant evertitur. Vix possim docendi legendos ei. Modo nemore ex vel, id vix nonumes signiferumque, id constituto comprehensam his.\n" +
                    "    Ludus legimus at pri, mei iisque civibus consequuntur ut. Ea per nulla persecuti. Duis porro adipiscing ius ne, id sit iudico cetero omittantur. Modo esse paulo vis ut, quaeque maluisset molestiae no ius, ea latine quaerendum persequeris eos. Purto tritani neglegentur at quo.\n" +
                    "    Scripta insolens pertinax cu vis. Eu vel dicta fastidii. Agam cibo utamur et usu, sed probo dicit te, an sit sale munere. Vim autem tritani volutpat an, cu tibique apeirian abhorreant sea. No veritus suscipit nam, id mei utamur legendos forensibus. Viderer diceret eos cu, legendos corrumpit disputando vim et, ullum primis eam id.\n" +
                    "    Sed ea malis nostrud prodesset, per te dolor utamur hendrerit. Solet ridens tamquam his te. An liber bonorum definiebas sea. Civibus omittam contentiones cu quo, hinc euismod omnesque pri eu, exerci delicata ei cum."


    };


    @Override
    public int getCount() {
        return tutorials.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    // Initilise the layout and intilise the tutorial layout :

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        mylLayoutInflater = (LayoutInflater) mycContext.getSystemService(mycContext.LAYOUT_INFLATER_SERVICE);

        View myView = mylLayoutInflater.inflate(R.layout.slider_, container,false);

        EditText slider_edittex = (EditText) myView.findViewById(R.id.editText2);

        slider_edittex.setText(tutorials[position]);

        container.addView(myView);
        return myView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);
    }
}
