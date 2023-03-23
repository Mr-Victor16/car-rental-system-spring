package com.example.carrentalsystem.Config;

import com.example.carrentalsystem.Models.*;
import com.example.carrentalsystem.Repositories.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Configuration
public class RepositoryInitializer {
    private final FuelTypeRepository fuelTypeRepository;

    private final RoleRepository roleRepository;

    private final CarRepository carRepository;

    private final BrandRepository brandRepository;

    private final CarModelRepository carModelRepository;

    private final CarImageRepository carImageRepository;

    public RepositoryInitializer(FuelTypeRepository fuelTypeRepository, RoleRepository roleRepository, CarRepository carRepository, BrandRepository brandRepository, CarModelRepository carModelRepository, CarImageRepository carImageRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
        this.roleRepository = roleRepository;
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.carImageRepository = carImageRepository;
    }

    @Bean
    InitializingBean init() {
        return () -> {
            if(fuelTypeRepository.findAll().isEmpty()){
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_DIESEL));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_GASOLINE));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_LPG));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_ELECTRIC));
                fuelTypeRepository.save(new FuelType(EFuelType.FUEL_HYBRID));
            }

            if(roleRepository.findAll().isEmpty()){
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
                roleRepository.save(new Role(ERole.ROLE_USER));
                roleRepository.save(new Role(ERole.ROLE_MODERATOR));
            }

            if(brandRepository.findAll().isEmpty()){
                brandRepository.save(new Brand("Audi"));
            }

            if(carModelRepository.findAll().isEmpty()){
                carModelRepository.save(new CarModel("A3"));
            }

            if(carRepository.findAll().isEmpty() && carImageRepository.findAll().isEmpty()){
                carImageRepository.save(new CarImage("1.png",imageFromURLToByteArray(new URL("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"))));
                carRepository.save(
                        new Car(
                                brandRepository.findByName("Audi"),
                                carModelRepository.findByName("A3"),
                                2013,
                                150000,
                                fuelTypeRepository.findByName(EFuelType.FUEL_DIESEL),
                                220,
                                "2.0",
                                550,
                                true,
                                carImageRepository.getByImageID(1)
                        )
                );

                carImageRepository.save(new CarImage("2.png",imageFromURLToByteArray(new URL("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"))));
                carRepository.save(
                        new Car(
                                brandRepository.findByName("Audi"),
                                carModelRepository.findByName("A3"),
                                2015,
                                155000,
                                fuelTypeRepository.findByName(EFuelType.FUEL_GASOLINE),
                                250,
                                "2.5",
                                600,
                                true,
                                carImageRepository.getByImageID(2)
                        )
                );
            }
        };
    }

    public static byte[] imageFromURLToByteArray(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(conn.getInputStream(), baos);

        return baos.toByteArray();
    }
}
