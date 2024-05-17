package com.example.proyecto_final.MainPages;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spanned;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.finalproyect.Fragments.FragmentoAlta;
import com.example.finalproyect.Fragments.FragmentoConsultaEmpleado;
import com.example.finalproyect.Fragments.FragmentoConsultarFichajesAdmin;
import com.example.finalproyect.Fragments.FragmentoNominas;
import com.example.finalproyect.Fragments.FragmentoSolicitudesAdmin;
import com.example.finalproyect.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainPageAdmin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    TextView textViewSaludar;
    TabLayout tabLayout;
    TextView fecha;

    FrameLayout frameLayout;
    final static String nombre = "EmpowerWork";
    final static String autor = "Nasratullah Jabarkhil";
    final static String versionApp = "EmpowerWork Version 1.0";
    final static String contacto = "nasratullah.jabarkhil@alumno.ucjc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_admin);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        textViewSaludar = findViewById(R.id.textoSludar);

        fecha = findViewById(R.id.diaYFecha);

        // Obtener la fecha y hora actual
        Date currentDate = new Date();

        // Formatear la fecha y hora actual según el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm EEEE dd MMMM", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Establecer el texto formateado en el TextView
        fecha.setText(formattedDate);

        // Obtiene la hora actual del sistema
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        int hora = Integer.parseInt(sdf.format(date));

        // Establece el saludo según la hora del día
        if (hora >= 6 && hora < 12) {
            textViewSaludar.setText("¡Buenos días!");
        } else if (hora >= 12 && hora < 18) {
            textViewSaludar.setText("¡Buenas tardes!");
        } else {
            textViewSaludar.setText("¡Buenas noches!");
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0,0);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int tabPosition = -1; // Inicializamos con un valor que no corresponde a ninguna pestaña

                if (item.getItemId() == R.id.alta) {
                    fragment = new FragmentoAlta();
                    tabPosition = 0; // Tab correspondiente al fragmento de alta
                } else if (item.getItemId() == R.id.consultarEmpleados) {
                    fragment = new FragmentoConsultaEmpleado();
                    tabPosition = 1; // Tab correspondiente al fragmento de consulta
                } else if (item.getItemId() == R.id.consultarFichajes) {
                    fragment = new FragmentoConsultarFichajesAdmin();
                    tabPosition = 2; // Tab correspondiente al fragmento de bajas
                }else if (item.getItemId() == R.id.consultarSolicitud) {
                    fragment = new FragmentoSolicitudesAdmin();
                    tabPosition = 3; // Tab correspondiente al fragmento de bajas
                }else if (item.getItemId() == R.id.nominas) {
                    fragment = new FragmentoNominas();
                    tabPosition = 4; // Tab correspondiente al fragmento de bajas
                } else if (item.getItemId() == R.id.acerca) {
                    // Mostrar información sobre la aplicación
                    mostrarVersionApp();
                } else if (item.getItemId() == R.id.cerrar) {
                    finish();
                    return true;
                }

                // Reemplazar el fragmento en el contenedor (frameLayout)
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);

                    // Establecer la pestaña activa
                    if (tabPosition != -1) {
                        tabLayout.getTabAt(tabPosition).select();
                    }
                }

                return true;
            }
        });



        tabLayout = findViewById(R.id.tabLayout);
        frameLayout = findViewById(R.id.frameLayout);


        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new FragmentoAlta()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentoAlta()).commit();
                }

                if(tab.getPosition() == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentoConsultaEmpleado()).commit();
                }
                if(tab.getPosition() == 2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentoConsultarFichajesAdmin()).commit();
                }
                if(tab.getPosition() == 3){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentoSolicitudesAdmin()).commit();
                }
                if(tab.getPosition() == 4){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentoNominas()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    // Método para mostrar la versión de la aplicación en un AlertDialog
    private void mostrarVersionApp() {
        // Concatena todos los mensajes que deseas mostrar en formato HTML
        String mensaje = "<b>Nombre:</b><br/>" + nombre + "<br/><br/>" +
                "<b>Versión:</b><br/>" + versionApp + "<br/><br/>" +
                "<b>Autor:</b><br/>" + autor + "<br/><br/>" +
                "<b>Contactar:</b><br/>" + contacto;

        // Convierte el mensaje HTML en un Spanned para que el texto en negrita funcione
        Spanned spannedMessage = HtmlCompat.fromHtml(mensaje, HtmlCompat.FROM_HTML_MODE_LEGACY);

        // Crear un TextView para mostrar el mensaje
        TextView textView = new TextView(this);
        textView.setText(spannedMessage);
        textView.setPadding(20, 20, 20, 20); // Añadir padding para separar el texto del borde del diálogo
        textView.setTextSize(16); // Tamaño del texto

        // Crear un LinearLayout para contener el logo y el texto
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        // Añadir el logo al LinearLayout
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.logo);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        layout.addView(imageView); // Añadir la imagen al LinearLayout

        // Añadir el TextView al LinearLayout
        layout.addView(textView); // Añadir el texto al LinearLayout

        // Crea el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de la aplicación");
        builder.setView(layout); // Establecer la vista personalizada con el logo y el texto
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cerrar el AlertDialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}