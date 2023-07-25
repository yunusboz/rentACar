package kodlama.io.rentACar.business.concretes;

import kodlama.io.rentACar.business.abstracts.ModelService;
import kodlama.io.rentACar.business.rules.ModelBusinessRules;
import kodlama.io.rentACar.core.utilities.mappers.ModelMapperService;
import kodlama.io.rentACar.dataAccess.abstracts.ModelRepository;
import kodlama.io.rentACar.dtos.requests.CreateModelRequest;
import kodlama.io.rentACar.dtos.responses.GetAllModelsResponse;
import kodlama.io.rentACar.entities.concretes.Model;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelManager implements ModelService {

    private ModelRepository modelRepository;
    private ModelMapperService mapper;
    private ModelBusinessRules modelBusinessRules;

    public ModelManager(ModelRepository modelRepository,
                        ModelMapperService mapper,
                        ModelBusinessRules modelBusinessRules) {
        this.modelRepository = modelRepository;
        this.mapper = mapper;
        this.modelBusinessRules = modelBusinessRules;
    }

    @Override
    public List<GetAllModelsResponse> getAll() {
        List<Model> models = modelRepository.findAll();

        List<GetAllModelsResponse> modelsResponse =
                models.stream()
                        .map(model -> mapper.forResponse().map(model, GetAllModelsResponse.class))
                        .collect(Collectors.toList());
        return modelsResponse;
    }

    @Override
    public void add(CreateModelRequest createModelRequest) {
        modelBusinessRules.checkIfModelNameExists(createModelRequest.getName());
        Model model = mapper.forResponse().map(createModelRequest,Model.class);
        modelRepository.save(model);
    }
}
