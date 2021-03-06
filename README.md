# Como escrever uma simples API RESTful usando Spring + Hibernate (ou morrer tentando)


Olá meu nobre rapaz, sim, você mesmo. Você estaria interessado em uns negócios diferenciados tipo... entrar na máfia ? Ah... desculpe, então você só quer aprender a escrever uma API em Java usando Spring? Bom, não importa, eu posso ensinar as duas coisas e elas funcionam de maneira bastante parecida na verdade. Então se você estiver interessado em segredos que podem custar sua vida, continue a leitura.

<img src="https://user-images.githubusercontent.com/27890590/115147623-461c4980-a032-11eb-8f22-6f4e97e9a13f.png">

O esquema dessa vez é bem simples, você está com sorte: precisamos fazer cadastros de usuários e fazer cadastros de endereços desses usuários. Mas, claro, tomando cuidado com armazenamento certo das informações e usando dos recursos disponíveis para avisar qualquer desvio do protocolo padrão (eu sei que você ja escreveu a data no lugar do nome durante a prova na escola, não adianta negar).

Então eu apresento a solução perfeita pra esse problema, a ***Famiglia Corleone***, oh, desculpe (*cof cof*), o ***Spring Framework***.
Então vamos ao funcionamento do nosso código.

# Funcionamento do código

Acompanhe meu raciocínio, o Spring funcionaria perfeitamente como um prédio da máfia, sim sim, acredite em mim. Teríamos 3 caixas de correio na entrada do prédio: duas delas seriam para entradas de mensagens com cadastros (que daqui para a frente chamaremos de requisições ou requests) e uma delas seria para a saída. Bom, e para simular o envio das requisições de cadastro suponhamos que teríamos um amigável carteiro hipotético (usaremos a ferramenta Postman, ela ilustrará alguns exemplos mais a diante). Dentro do prédio os membros da *famiglia* cuidariam de verificar e direcionar as requisições de cadastro recebidas para o pessoal especializado.

![image](https://user-images.githubusercontent.com/27890590/115147581-0a817f80-a032-11eb-8f13-415a015b1b55.png)

Ah, desculpe, eu perco o fio da meada muito fácil, vamos voltar a falar do código. Onde nós iremos guardar os cadastros feitos para que eles possam ser consultados posteriormente? Usaremos uma API chamada JPA (Java Persistence API) e um banco de dados baseado em SQL, o PostgreSQL. A JPA foi escolhida porque ela é bastante prática: ela ficará responsável pelo ORM no nosso código (Object-Relational Mapping), ou seja, podemos escrever classes no nosso código que serão interpretadas pela JPA como entidades que terão tabelas correspondentes no banco de dados. Isso facilita muito o nosso trabalho, pois as _queries_ mais recorrentes que precisaríamos fazer têm métodos correspondentes implementados na JPA. Mas, caso precisemos fazer usos de _queries_ específicas, nós mesmos podemos escrevê-las usando anotações da JPA ou podemos usar um outro recurso muito útil _também_ da JPA, os _Derived Query Methods_, o que eu acho preferível, porque eles aumentam a portabilidade do código (essas tecnologias modernas são realmente impressionantes não é mesmo ?).

>_Pense no JPA como o responsável pela sala de arquivos, ele é um rapaz bem simpático e está sempre disposto a ajudar. Hoje ele está particularmente animado porque finalmente apareceu uma utilidade pro novo armário dele, um armário onde as gavetas têm mais gavetas dentro, ele foi produzido por uma marca muito boa, a PostgreSQL._

Usaremos a IDE Eclipse para escrever o código, pois ela já organiza muito bem a estrutura de arquivos em uma aplicação java e tem vários módulos e funcionalidades embutidos que nos permitem ser mais ágeis na escrita do código.

Certo, certo, garoto, eu sei que você quer saber o que acontece dentro do prédio... então por onde nós começaremos a fazer isso tudo ?

### CRIANDO O PRÉDIO ORA BOLAS

Podemos iniciar um novo projeto spring pelo site [start.spring.io](https://start.spring.io/), nesse site selecionaremos nossas especificações de projeto, as mais importantes são:

* Java (versão 8) como linguagem de programação
* Maven como gerenciador de dependências
* As dependências são:
    * Spring Web
    * Spring Data JPA
    * PostgreSQL driver
    * Hibernate validation

Quanto a gerenciamento de dependências, o Maven é uma ótima ferramenta, pois, com ele, precisamos apenas declarar as dependências que queremos num único arquivo, o pom.xml, e ele ficará responsável pelo resto: baixar todas as bibliotecas que precisamos e também as bibliotecas que essas bibliotecas precisam.

>_Pense que todos dentro do nosso prédio se comunicam em uma linguagem secreta chamada Java e pense no Maven como se fosse um daqueles contatos esquisitos que todo mafioso tem, ele manda alguns capangas para nós de acordo com nossas demandas, esses capangas são as dependências._

Bom, depois disso importamos o projeto no Eclipse e escrevemos no arquivo application.properties algumas configurações para que a relação entre o JPA e o PostgreSQL esteja nos conformes (informações de acesso ao banco de dados, como url, login, senha, dialeto de SQL usado...).

>_Sim, o nosso garoto JPA está pronto para começar a usar o armário novo que ele comprou._

### AGORA SIM, VAMOS AOS DEPARTAMENTOS DO PRÉDIO E SUAS FUNÇÕES

Nossa aplicação terá 5 pacotes principais, um para cada categoria de classes a seguir:
* Models
* Controllers
* Repositories
* Services
* Exception handlers

## O departamento de Model

Escreveremos três classes no pacote de model, uma para o cadastro de usuários, uma para o cadastro de endereço dos usuários e uma terceira para encapsular um usuário com seus respectivos endereços num único objeto. Nas duas primeiras classes escreveremos anotações (como a @NotBlank) que nos ajudarão com validações nas informações dos cadastros, com destaques para as anotações de e-mail e cpf (@Email e @CPF), pois elas verificam a coerência dos dados informados. Essas anotações são do Bean Validation e do Hibernate, eles facilitam o trabalho de verificar se os dados recebidos fazem algum sentido (é como diz aquele ditado: o usuário escreve torto por linhas tortas).

Também temos anotações do JPA para criar uma _entidade_ e sua respectiva tabela no banco de dados (@Entity e @Table); para geração de ids automáticos dentro da tabela (@Id e @GeneratedValue(strategy = GenerationType.IDENTITY)), assim não teremos que nos preocupar com a ordem do armazenamento de informação; por fim, há anotações para colunas com as informações desejadas na tabela (@Column) e suas respectivas exigências (como _"unique = true"_, para garantir que cpf e e-mail não se repitam no cadastro de usuários).

>_As classes de model são como envelopes contendo a informação  das requisições que nós queremos armazenar no arquivo, elas serão entregues ao nosso querido JPA e ele vai guardar elas corretamente seguindo orientações de notas especiais. Essas notas foram escritas pelo Hibernate, um dos capangas do Maven, quer saber como ele sabe disso tudo? Digamos que ele tem os próprios _métodos_ se é que você me entende._

_Nota: os snippets exibidos a seguir não contêm getters e setters para que foquemos só no que é mais essencial._
~~~Java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "username")
    @NotBlank
    private String username;
    
    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    private String email;
    
    @NotBlank
    @CPF
    @Column(name = "cpf", unique = true)
    private String cpf;
    
    @Column(name = "birthday", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
} 
~~~
*Snippet da classe model de usuário*


&nbsp;
~~~Java
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
    @NotBlank
    @Column(name = "logradouro")
    private String logradouro;
	
    @NotBlank
    @Column(name = "numero")
    private String numero;
	
    @NotBlank
    @Column(name = "complemento")
    private String complemento;
	
    @NotBlank
    @Column(name = "bairro")
    private String bairro;
	
    @NotBlank
    @Column(name = "cidade")
    private String cidade;
	
    @NotBlank
    @Column(name = "estado")
    private String estado;
	
    @NotBlank
    @Column(name = "cep")
    private String cep;
	
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
~~~
*Snippet da classe model de endereço*


&nbsp;

No nosso model de endereços temos uma coluna especial com as anotações @ManyToOne e @JoinColumn(name = "user_id", nullable = false), essa coluna vai ser responsável por relacionar cada endereço ao seu respectivo usuário numa relação do tipo _Many to One_, pois um usuário pode ter vários endereços.

>_Deu pra entender por que as gavetas dentro de gavetas são importantes agora né?_

Por fim, a classe de "usuário e endereços", que tem uma estrutura bem simples.

~~~Java
public class UserAndAddresses {
    private User user;
    private List<Address> addresses;
}
~~~
*Snippet da classe de "usuário e endereços"*


&nbsp;

## O departamento de Controller

No pacote de controller, escrevemos uma classe para ser nosso controller. Nessa classe, escreveremos a anotação @RestController do Spring, que define ela como um controller do Spring, ou seja, uma classe que recebe e direciona requisições para os métodos certos. Também escreveremos anotações de _"mapping"_ (@RequestMapping, @GetMapping e @PostMapping), elas servem para para especificar onde e o que será feito nos _endpoints_ utilizados na aplicação. No nosso caso, temos 3 endpoints: dois de *POST*, um para cada tipo de cadastro e um de *GET* para obter informações de um usuário já cadastrado).

~~~Java
@RestController
@RequestMapping("/api")
public class UserController {
	
    @Autowired
    private UserService service;
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserAndAddresses> retrieveUserData(@PathVariable(value = "id") Long userId)
    	    throws ResourceNotFoundException {
        UserAndAddresses userdata = service.getUserInformationById(userId);

        return ResponseEntity.ok().body(userdata);
    }
    
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser(@Validated @RequestBody User user, BindingResult br)
    		throws DataIntegrityViolationException, Exception {
    	if (br.hasErrors())
    		throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
    	return this.service.createUser(user);
    }
    
    @PostMapping("/users/address/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Address createAddress(@PathVariable(value = "id") Long userId, @Validated @RequestBody Address address, BindingResult br)
    		throws ResourceNotFoundException, DataIntegrityViolationException, Exception {
    	if (br.hasErrors())
    		throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
    	return this.service.createAddress(userId, address);
    }
}
~~~
*Snippet da classe de controller*


&nbsp;

Vale notar que, no método de *POST* para cadastrar endereço (createAddress), recebemos o id do usuário pela _URL_ ({id}), isso serve para que relacionemos o cadastro de endereço ao usuário correto

>_Esses endpoints são as caixas de correio que eu mencionei anteriormente e o controller é como o padrinho da máfia, o manda chuva pra quem todo mundo pede favor, ele já tem um pessoal pronto e selecionado pra cada favor que pedirem pra ele, ou, no caso, para cada tipo de requisição que chegar pelas caixas de correio._

![image](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJIs8RsK0H1692Q4BGUVU9RcE88d4KuJ2-LA&usqp=CAU)

Também temos uma outra anotação importante nessa classe, a anotação @Autowired, responsável por um dos principais benefícios do Spring: a injeção de dependências. Precisamos dessa anotação para poder usar no nosso controller os métodos de um objeto do tipo service (injetando-a na classe de controller), perceba também que o objeto do tipo service foi criado, mas não foi iniciado, o spring iniciará ele quando for necessário.

>_Porque de vez em quando entra gente nova na tarefa, mas é gente de confiança, claro_

## O departamento de Repository

No pacote de repositories temos apenas duas classes que estendem a classe JpaRepository e herdam métodos úteis para que possamos usar nosso banco de dados para salvar as informações desejadas.

>_O rapaz JPA trabalha no departamento de repository, ele tem os seus pupilos, o UserRepository e o AddressRepository, ele ensinou muito bem para eles como se usa a sala do arquivo._

~~~Java
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
~~~
*Snippet da classe de respository de usuário*


&nbsp;

~~~Java
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    public List<Address> findByUserId(Long postId);

}
~~~
*Snippet da classe de repository de endereço*


&nbsp;

Vale notar que, na classe de endereço, usamos o recurso que eu mencionei anteriormente, criamos um _Derived Query Method_ (findByUserId), para que possamos obter todos os endereços que pertencem a um mesmo usuário quando for necessário.

## O departamento de Service

Nesse pacote, temos a classe de service, que contém métodos fundamentais para o funcionamento da aplicação. Esses métodos serão chamados pelo controller para atender as requisições que ele receber em cada endpoint.

~~~Java
@Service
public class UserService {
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressRepository addressRepository;
	
    public UserAndAddresses getUserInformationById(Long userId) throws ResourceNotFoundException {
    	User user = userRepository.findById(userId).orElseThrow(() -> new 
		ResourceNotFoundException("There is no User for the id " + userId));
        
	List<Address> addresses = addressRepository.findByUserId(userId);
        UserAndAddresses userdata = new UserAndAddresses(user, addresses);
        
        return userdata;
    }
    
    public User createUser(User user) throws DataIntegrityViolationException, Exception{
    	try {
    		return userRepository.save(user);
    	}
    	catch(DataIntegrityViolationException e){
    		throw new ConstraintException("Constraint Problem - " + e.getMostSpecificCause().getMessage());
    	}
    	catch(Exception e) {
    		throw new Exception("Unknown error, but here is some info: " + e.getMessage());
    	}
    }
    
    public Address createAddress(Long userId, Address address)
    	        throws ResourceNotFoundException, DataIntegrityViolationException, Exception{
    	User user = userRepository.findById(userId).orElseThrow(() -> new 
		ResourceNotFoundException("There is no User for this id: " + userId));
    	
	address.setUser(user);
    	
    	try {
    		return addressRepository.save(address);
    	}
    	catch(DataIntegrityViolationException e){
    		throw new ConstraintException("Constraint Problem - " + e.getMostSpecificCause().getMessage());
    	}
    	catch(Exception e) {
    		throw new Exception("Unknown error, but here is some info: " + e.getMessage());
    	}
    }
}
~~~
*Snippet da classe de service*


&nbsp;

Logo de cara dá pra perceber mais dois @Autowired ali, injetamos objetos das duas classes de repository que, como eu expliquei anteriormente, contêm métodos para que acessemos o banco de dados. Temos métodos para registrar os usuários, para registrar os endereços de um usuário e para exibir um usuário com todos os seus endereços.

Vale notar também que aqui usaremos aquela terceira classe que eu mencionei no pacote de model: o método getUserInformationById retorna um objeto contendo um usuário e sua lista de endereços.

Caso algo dê errado no processo, teremos uma noção de onde pode ter ocorrido o erro devido às classes de exception handling que serão explicadas a seguir.

## O departamente de Exception handling

Nesse pacote teremos 4 classes responsáveis por tratar os eventuais erros que possam acontecer durante o processo de cadastro e de consulta de informações no banco de dados. Essas classes são mostradas a seguir:

~~~Java
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConstraintException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ConstraintException(String msg) {
		super(msg);
	}
	
	public ConstraintException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
~~~
*Snippet da classe ConstraintException*


&nbsp;

A classe de ConstraintException estende a classe de RuntimeException e assim podemos herdar seus métodos de exibição de mensagens no caso de alguma de nossas validações não ser respeitada

~~~Java
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message){
        super(message);
    }
}
~~~
*Snippet da classe ResourceNotFoundException*


&nbsp;

A classe de ResourceNotFoundException estende a classe de Exception e assim podemos herdar seus métodos de exibição de mensagens no caso de alguma de nossos usuários não ser encontrado no banco de dados.

~~~Java
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
~~~
*Snippet da classe ErrorDetails*


&nbsp;

A classe ErrorDetails será usada encapsular informações importantes sobre a Exception num único objeto.

~~~Java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
         ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
         return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<?> constraintException(ConstraintException ex, WebRequest request) {
         ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
         return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
~~~
*Snippet da classe GlobalExceptionHandler*


&nbsp;

Por fim, temos a classe mais importante do pacote, a classe GlobalExceptionHandler, que será responsável por retornar informações encapsuladas num objeto do tipo ErrorDetails aos endpoints em que as exceptions definidas pelas classes RuntimeException e ResourceNotFoundException ocorrerem. Note que ela tem a anotação @ControllerAdvice do Spring que a define como um "centro de tratamendo de erros" e, acima dos seus métodos, temos as anotações que definem qual método será chamado para cada tipo de Exception definida.

>_O pessoal do departamento de exception handling é bem eficiente, eles têm uns gestos esquisitos pra se comunicar, mas tudo bem_
<img width="400" height="320" src="https://user-images.githubusercontent.com/27890590/115148324-6c8fb400-a035-11eb-99f1-5f5c8c25b4d4.png">

*Típico funcionário do departamento de exception handling num dia de trabalho normal*


&nbsp;

## Testes com o Postman

Agora iremos usar a ferramenta Postman para demonstrar o funcionamento do nosso código. O Postman será usado para enviar e receber requests aos endpoints que programamos e com ele será possível ver também os status de _http_ que receberemos em cada caso.

>_Ou chamaremos nosso carteiro hipotético, por assim dizer._

![image](https://user-images.githubusercontent.com/27890590/115165433-46ddcb80-a084-11eb-89f3-a587d2301586.png)

*imagem de erro no cadastro de usuário, com status retornado 400*


&nbsp;

![image](https://user-images.githubusercontent.com/27890590/115165525-9ae8b000-a084-11eb-9fda-8567d9f518b7.png)

*imagem de criação do cadastro de usuário, com status retornado 201*


&nbsp;

![image](https://user-images.githubusercontent.com/27890590/115165533-a3d98180-a084-11eb-9c5f-0fa22a9a742c.png)

*imagem de erro no cadastro de endereço, com status retornado 400*


&nbsp;

![image](https://user-images.githubusercontent.com/27890590/115165536-ab008f80-a084-11eb-9ec0-466477297783.png)

*imagem de criação do cadastro de endereço, com status retornado 201*


&nbsp;

![image](https://user-images.githubusercontent.com/27890590/115165539-b18f0700-a084-11eb-80ac-6e527a52b33d.png)

*imagem de erro de consulta de usuário cadastrado, com status retornado 404*


&nbsp;

![image](https://user-images.githubusercontent.com/27890590/115165543-b653bb00-a084-11eb-98b4-ee7208c61c42.png)

*imagem de consulta correta de usuário cadastrado, com status retornado 200*


&nbsp;

## Uma última consideração

Ah, eu quase me esqueci, meu jovem... talvez eu tenha falado um pouquinho mais do que eu deveria. Mas, bom, eu avisei para você que eram segredos que poderiam custar a sua vida, então eu lamento garoto... hohoho... você não pode mais continuar vivo, você sabe demais agora. Você tem 5 minutos para correr.

![image](https://user-images.githubusercontent.com/27890590/115148634-e07e8c00-a036-11eb-9e0f-4bb729d20184.png)
