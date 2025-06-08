//package dev.jda.api.library.mapper;
//
//import dev.jda.api.library.entity.Profile;
//import dev.jda.model.library.dto.ProfileDTO;
//import dev.jda.model.library.dto.UserDTO;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Component;
//
//import static org.modelmapper.Conditions.isNotNull;
//
//@Component
//public class ProfileMapper extends BaseMapper implements Mapper{
//
//    @Override
//    public void createMapper(ModelMapper mm) {
//        mm.createTypeMap(Profile.class, ProfileDTO.class)
//                .addMappings(mapper -> {
//                    mapper.when(isNotNull()).using(stringToLocalDateTime).map(UserDTO::getDate, ProfileDTO::getDate);
//                });
//    }
//}
//
