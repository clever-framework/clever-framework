package io.github.toquery.framework.data.rest;

import io.github.toquery.framework.web.controller.AppBaseWebController;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.data.rest.core.mapping.ResourceType;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppEntityRestController extends AppBaseWebController {

    private static final String BASE_MAPPING = "/{entity-rest}";

    /**
     * todo
     *  org.springframework.validation.BeanPropertyBindingResult: 1 errors
     *  Field error in object 'defaultedPageable' on field 'isDefault': rejected value [null]; codes [typeMismatch.defaultedPageable.isDefault,typeMismatch.isDefault,typeMismatch.boolean,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [defaultedPageable.isDefault,isDefault]; arguments []; default message [isDefault]]; default message [Failed to convert value of type 'null' to required type 'boolean'; nested exception is org.springframework.core.convert.ConversionFailedException: Failed to convert from type [null] to type [boolean] for value 'null'; nested exception is java.lang.IllegalArgumentException: A null value cannot be assigned to a primitive type]
     *
     * <code>GET /{repository}</code> - Returns the collection resource (paged or unpaged).
     *
     * @param resourceInformation
     * @param pageable
     * @param sort
     * @param assembler
     * @return
     * @throws ResourceNotFoundException
     * @throws HttpRequestMethodNotSupportedException

    @ResponseBody
    @RequestMapping(value = BASE_MAPPING, method = RequestMethod.GET)
    public String getCollectionResource(@QuerydslPredicate RootResourceInformation resourceInformation,
                                        DefaultedPageable pageable, Sort sort, PersistentEntityResourceAssembler assembler)
            throws ResourceNotFoundException, HttpRequestMethodNotSupportedException {

        resourceInformation.verifySupportedMethod(HttpMethod.GET, ResourceType.COLLECTION);

        RepositoryInvoker invoker = resourceInformation.getInvoker();

        if (null == invoker) {
            throw new ResourceNotFoundException();
        }

        Iterable<?> results = pageable.getPageable() != null //
                ? invoker.invokeFindAll(pageable.getPageable()) //
                : invoker.invokeFindAll(sort);


        return "String";
    }
     */
}
