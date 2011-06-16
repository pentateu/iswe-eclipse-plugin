package nz.co.iswe.generator.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Generator {
	//Contem as configuracoes para Listas Many to One
	ManyToOneType [] manyToOneConfigs() default {}; 
	
	//possui as configuracoes sobre upload.
	UploadType [] uploadConfigs() default {};
	
    //Informa a estrategia de seguranca que sera aplicada.
	SecurityStrategyType [] securityStrategy() default{};
	
	//Label do campo utilizado nos objetos da camanda view
	String label() default "";
	
    //Informa o tipo de visualizacao da propriedade
    ViewType [] viewType() default{ViewType.VIEW_ALL};
    
	//Informa se o campo é a descricao da entidade
	boolean campoDescricao() default false;
	
	//Informa que é uma referencia a entidade mestre
	boolean entidadeMestre() default false;
	
	boolean campoSenha() default false;
	
	MatchMode matchMode() default MatchMode.ANYWHERE;

	boolean keyList() default false;
	
}