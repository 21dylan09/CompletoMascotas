package com.example.loginsignup.baseDatos.entidades;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import com.example.loginsignup.baseDatos.dao.*;


@Database(entities = {Usuario.class, Mascota.class, CitaVeterinaria.class, Estado.class, Enfermedad.class, HistorialMedico.class, Restriccion.class, EnfermedadCronica.class, Alergia.class, NotaMascota.class, Gasto.class, Ubicacion.class, RegistroPeso.class}, version = 6)

public abstract class BaseDatos extends RoomDatabase {
    private static volatile BaseDatos INSTANCE;

    public abstract UsuarioDao usuarioDao();
    public abstract MascotaDao mascotaDao();
    public abstract EstadoDao estadoDao();
    public abstract EnfermedadDao enfermedadDao();
    public abstract HistorialDao historiaDao();
    public abstract RestriccionDao restriccionDao();
    public abstract AlergiaDao alergiaDao();
    public abstract EnfermedadCronicaDao enfermedadCronicaDao();
    public abstract NotaMascotaDao notaMascotaDao();
    public abstract GastoDao gastoDao();
    public abstract UbicacionDAO ubicacionDAO();
    public abstract RegistroPesoDAO registroPesoDAO();

    public static BaseDatos getBaseDatos(final Context context){
        if (INSTANCE == null) {
            synchronized (BaseDatos.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BaseDatos.class, "aplicacion_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback(){
                                @Override
                                public void onCreate(SupportSQLiteDatabase db){
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        EstadoDao estadoDao = INSTANCE.estadoDao();
                                        estadoDao.insertarEstado(new Estado(1, "Pendiente"));
                                        estadoDao.insertarEstado(new Estado(2, "Terminada"));
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
