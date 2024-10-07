package exercise.mapper;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

// BEGIN
@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductMapper {
    @Mapping(target = "category.id", source = "categoryId")
    public abstract Product map(ProductCreateDTO productCreateDTO);
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    public abstract ProductDTO map(Product product);

    public abstract void update(ProductUpdateDTO productUpdateDTO, @MappingTarget Product product);
}
// END
