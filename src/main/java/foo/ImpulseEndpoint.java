package foo;

import java.io.Console;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.TransactionRolledbackException;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.discovery.DiscoveryGenerator.Result;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.client.util.Data;
import com.google.api.server.spi.auth.EspAuthenticator;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.prospectivesearch.ErrorPb.Error;
import com.google.appengine.repackaged.com.google.datastore.v1.client.DatastoreException;

import endpoints.repackaged.com.google.api.Endpoint;
import endpoints.repackaged.org.apache.http.util.EntityUtils;

import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.*;


@Api(name = "impulseAPI",
     version = "v2",
     audiences = "943253209248-viavhim27kiij8eb6fepitrbmmu67rof.apps.googleusercontent.com",
  	 clientIds = {"943253209248-viavhim27kiij8eb6fepitrbmmu67rof.apps.googleusercontent.com",
        "943253209248-viavhim27kiij8eb6fepitrbmmu67rof.apps.googleusercontent.com"},
     namespace =
     @ApiNamespace(
		   ownerDomain = "Impulse.local",
		   ownerName = "Impulse.local",
		   packagePath = "")
)


public class ImpulseEndpoint {
    /*######################################### VERSION SANS FILTRE PERSONNALISABLES #########################################*/
    /*
    //RECUPERATION DE TOUTES LES PETITIONS
    @ApiMethod(name = "ListAllPetitions", path="ListAllPetitions/{page}", httpMethod = HttpMethod.GET)
	public List<Entity> ListAllPetitions(@Named("page") int page) {
		Query P_ListAll_q = new Query("Petition").addSort("P_Signatures", SortDirection.DESCENDING);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery P_ListAll_pq = datastore.prepare(P_ListAll_q);

        int startIndex = (page - 1) * 25;

		List<Entity> result = P_ListAll_pq.asList(FetchOptions.Builder.withOffset(startIndex).limit(25));
		return result;
	}

    //RECUPERATION DES PETITIONS SIGNEES PAR UNE PERSONNE
    @ApiMethod(name = "ListSignedPetitions", path ="ListSignedPetitions/{authorEmail}/{page}", httpMethod = HttpMethod.GET)
    public List<Entity> ListSignedPetitions(@Named("page") page, @Named("authorEmail") String authorEmail)
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Query authorQuery = new Query("Author").setFilter(new FilterPredicate("A_Email", FilterOperator.EQUAL, authorEmail));
        Entity author = datastore.prepare((authorQuery)).asSingleEntity();

        List<Entity> returnTab = new ArrayList<Entity>();
        List<Key> signedPetitionsKeys = (ArrayList<Key>) author.getProperty("A_Signed");

        int startIndex = (page - 1) * 25;
        int endIndex = Math.min((page + 1) * 25, signedPetitionsKeys.size());
        
        for(int i = startIndex; i < endIndex; i++)
        {
            Key signedKey = signedPetitionsKeys.get(i);
            Query petitionQuery = new Query("Petition").setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, signedKey));
            returnTab.add(datastore.prepare(petitionQuery).asSingleEntity());
        }

        return returnTab;
    }

    //RECUPERATION D'UNE PETITION PAR NOM D'AUTEUR
    @ApiMethod(name = "ListPetitionsByAuthorName", path="ListPetitionsByAuthorName/{authorEmail}/{page}", httpMethod = HttpMethod.GET)
    public List<Entity> ListPetitionsByAuthorName(@Named("authorEmail") String authorEmail, @Named("page") int page) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        Query authorQuery = new Query("Author").setFilter(new FilterPredicate("A_Email", FilterOperator.EQUAL, authorEmail));
        Entity author = datastore.prepare(authorQuery).asSingleEntity();
        
        if (author == null) {
            throw new IllegalArgumentException("Author not found with name: " + authorEmail);
        }
        
        
        Query petitionQuery = new Query("Petition").setFilter(new FilterPredicate("P_Author", FilterOperator.EQUAL, author.getKey()));
        petitionQuery.addSort("P_Signatures", SortDirection.DESCENDING);
        PreparedQuery preparedQuery = datastore.prepare(petitionQuery);

        int startIndex = (page - 1) * 25;

        List<Entity> result = preparedQuery.asList(FetchOptions.Builder.withOffset(startIndex).limit(25));
        
        return result;
    }
    */
    /*###################################################### END #######################################################*/

    /*######################################### VERSION FILTRE PERSONNALISABLES #########################################*/
    //RECUPERATION DE TOUTES LES PETITIONS
    @ApiMethod(name = "ListAllPetitions", path="ListAllPetitions/{filter}/{sort}/{page}", httpMethod = HttpMethod.GET)
	public List<Entity> ListAllPetitions(@Named("filter") String filterCfg, @Named("sort") String sortCfg, @Named("page") int page) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        String filter = "";
        switch(filterCfg) {
            case "AUTHOR" : 
                filter = "P_Author";
                break;
            case "NAME" :
                filter = "P_Name";
                break;
            case "DATE" :
                filter = "P_Date";
                break;
            case "SIGNATURES" :
                filter = "P_Signatures";
                break;
            default :
                filter = "P_Date";
                break;
        }

        SortDirection sort = null;
        switch (sortCfg) {
            case "ASC" :
                sort = SortDirection.ASCENDING;
                break;

            case "DESC" :
                sort = SortDirection.DESCENDING;
                break;

            default :
                sort = SortDirection.DESCENDING;
                break;
        }


        Query P_ListAll_q = new Query("Petition").addSort(filter, sort);
        PreparedQuery P_ListAll_pq = datastore.prepare(P_ListAll_q);
        
        int startIndex = (page - 1)  * 3;

		List<Entity> result = P_ListAll_pq.asList(FetchOptions.Builder.withOffset(startIndex).limit(3));
		return result;
        
	}

    //RECUPERATION DES PETITIONS SIGNEES PAR UNE PERSONNE
    @ApiMethod(name = "ListSignedPetitions", path ="ListSignedPetitions/{filter}/{sort}/{authorEmail}/{page}", httpMethod = HttpMethod.GET)
    public List<Entity> ListSignedPetitions(@Named("filter") String filterCfg, @Named("sort") String sortCfg ,@Named("authorEmail") String authorEmail, @Named("page") int page)
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query authorQuery = new Query("Author").setFilter(new FilterPredicate("A_Email", FilterOperator.EQUAL, authorEmail));
        Entity author = datastore.prepare((authorQuery)).asSingleEntity();

        List<Entity> returnTab = new ArrayList<Entity>();
        List<Key> signedPetitionsKeys = (ArrayList<Key>) author.getProperty("A_Signed");

        int startIndex = (page - 1) * 3;
        int endIndex = Math.min((page + 1) * 3, signedPetitionsKeys.size());
        
        for(int i = startIndex; i < endIndex; i++)
        {
            Key signedKey = signedPetitionsKeys.get(i);
            Query petitionQuery = new Query("Petition").setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, signedKey));
            returnTab.add(datastore.prepare(petitionQuery).asSingleEntity());
        }

        Collections.sort(returnTab, new Comparator<Entity>() {

            public int compare(Entity entity1, Entity entity2) {
                String filter = "";
                int result = -1000;
                switch(filterCfg) {
                    case "AUTHOR" : 
                        filter = "P_Author";
                        String authA = (String) entity1.getProperty(filter);
                        String authB = (String) entity2.getProperty(filter);
                        result = authA.compareTo(authB);
                        break;
                        
                    case "NAME" :
                        filter = "P_Name";
                        String nameA = (String) entity1.getProperty(filter);
                        String nameB = (String) entity2.getProperty(filter);
                        result = nameA.compareTo(nameB);
                        break;

                    case "DATE" :
                        filter = "P_Date";
                        Date dateA = (Date) entity1.getProperty(filter);
                        Date dateB = (Date) entity2.getProperty(filter);
                        result = dateA.compareTo(dateB);

                        break;

                    case "SIGNATURES" :
                        filter = "P_Signatures";
                        int signA = (int) entity1.getProperty(filter);
                        int signB = (int) entity2.getProperty(filter);
                        if(signA == signB){result = 0; } else if(signA <= signB){result = -1; } else {result = 1;};
                        
                        break;

                    default :
                        filter = "P_Date";
                        break;
                }

                return result;
                
            }
        });

        // Inversion du tri si nécessaire (DESCENDING)
        if (sortCfg == "DESC") {
            Collections.reverse(returnTab);
        }

        return returnTab;
    }

    //RECUPERATION D'UNE PETITION PAR NOM D'AUTEUR
    @ApiMethod(name = "ListPetitionsByAuthorName", path="ListPetitionsByAuthorName/{filter}/{Sort}/{authorEmail}/{page}", httpMethod = HttpMethod.GET)
    public List<Entity> ListPetitionsByAuthorName(@Named("filter") String filterCfg, @Named("sort") String sortCfg, @Named("authorEmail") String authorEmail, @Named("page") int page) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        
        String filter = "";
        switch(filterCfg) {
            case "AUTHOR" : 
                filter = "P_Author";
                break;
            case "NAME" :
                filter = "P_Name";
                break;
            case "DATE" :
                filter = "P_Date";
                break;
            case "SIGNATURES" :
                filter = "P_Signatures";
                break;
            default :
                filter = "P_Date";
                break;
        }

        SortDirection sort = null;
        switch (sortCfg) {
            case "ASC" :
                sort = SortDirection.ASCENDING;
                break;

            case "DESC" :
                sort = SortDirection.DESCENDING;
                break;

            default :
                sort = SortDirection.DESCENDING;
                break;
        }

        
        Query authorQuery = new Query("Author").setFilter(new FilterPredicate("A_Email", FilterOperator.EQUAL, authorEmail));
        Entity author = datastore.prepare(authorQuery).asSingleEntity();
        
        if (author == null) {
            throw new IllegalArgumentException("Author not found with name: " + authorEmail);
        }
        
        
        Query petitionQuery = new Query("Petition").setFilter(new FilterPredicate("P_Author", FilterOperator.EQUAL, author.getKey()));
        petitionQuery.addSort(filter, sort);
        PreparedQuery preparedQuery = datastore.prepare(petitionQuery);

        int startIndex = (page - 1)  * 3;

        List<Entity> result = preparedQuery.asList(FetchOptions.Builder.withOffset(startIndex).limit(3));
        
        return result;
    }
    
    /*################################################### END  ####################################################*/



    

    //RECUPERATION DE TOUS LES AUTEURS
    @ApiMethod(name = "ListAllAuthors", httpMethod = HttpMethod.GET)
    public List<Entity> ListAllAuthors() {
        Query A_ListAll_q = new Query("Author").addSort("A_Name", SortDirection.DESCENDING);
    
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
        PreparedQuery A_ListAll_pq = datastore.prepare(A_ListAll_q);
        List<Entity> result = A_ListAll_pq.asList(FetchOptions.Builder.withLimit(100));
        return result;
    }
    
        
    //RECUPERATION D'UN AUTEUR
    @ApiMethod(name = "GetAuthor", path="GetAuthor/{authorId}", httpMethod = HttpMethod.GET)
    public Entity GetAuthor(@Named("authorId") String authorId) {
    
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key EGPetitions = KeyFactory.createKey("EGPetitions", "EGPetitions");
            
        Key authorKey = KeyFactory.createKey(EGPetitions,"Author", Long.parseLong(authorId));
        Query.Filter keyFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, authorKey);
        Query authorQuery = new Query("Author").setFilter(keyFilter);
            
        Entity authorG = datastore.prepare(authorQuery).asSingleEntity();
            
        if (authorG == null) {
            return null;
        }
        return authorG;
            
    }
    
    //RECUPERATION D'UNE PETITION
    @ApiMethod(name = "GetPetition", path="GetPetition/{petId}", httpMethod = HttpMethod.GET)
    public Entity GetPetition(@Named("petId") String petId) {
            
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key EGPetitions = KeyFactory.createKey("EGPetitions", "EGPetitions");
    
        Key petitionKey = KeyFactory.createKey(EGPetitions,"Petition", Long.parseLong(petId));
        Query.Filter keyFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, petitionKey);
        Query petitionQuery = new Query("Petition").setFilter(keyFilter);
    
        Entity petitionG = datastore.prepare(petitionQuery).asSingleEntity();
    
        if (petitionG == null) {
            return null;
        }
    
        return petitionG;
    }

    //CREATION DE PETITIONS
    @ApiMethod(name = "AddPetition", path = "AddPetition/{ename}/{edescription}/{eemail}/{eauthor}", httpMethod = HttpMethod.PUT)
    public Entity AddPetition(@Named("ename") String Petition_name, @Named("edescription") String Petition_decription, @Named("eemail") String Petition_email, @Named("eauthor") String Petition_author) throws IOException {
        
        System.setProperty(DatastoreServiceConfig.DATASTORE_EMPTY_LIST_SUPPORT, Boolean.TRUE.toString());

        ArrayList<Key> AuthorSigTab = new ArrayList<Key>();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key EGPetitions = KeyFactory.createKey("EGPetitions", "EGPetitions");
        
        Query.Filter nameFilter = new Query.FilterPredicate("A_Name", Query.FilterOperator.EQUAL, Petition_author);
        Query.Filter emailFilter = new Query.FilterPredicate("A_Email", Query.FilterOperator.EQUAL, Petition_email);
        Query.Filter compositeFilter = Query.CompositeFilterOperator.and(nameFilter, emailFilter);
        Query query = new Query("Author").setFilter(compositeFilter);


        Entity author = datastore.prepare(query).asSingleEntity();
        Key authorKey = null;

        if (author == null) {
            try{
                Entity newAuthor = new Entity("Author", EGPetitions);
                authorKey = newAuthor.getKey();
                newAuthor.setProperty("A_Name", Petition_author);
                newAuthor.setProperty("A_Email", Petition_email);
                newAuthor.setProperty("A_Signed", AuthorSigTab);
                datastore.put(newAuthor);
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new IOException("500a - UNKNOW INTERNAL ERROR");
            }
        }

        Entity petition = new Entity("Petition",EGPetitions);
        try{
            petition.setProperty("P_Name", Petition_name);
            petition.setProperty("P_Author", authorKey);
            petition.setProperty("P_Description", Petition_decription);
            petition.setProperty("P_Signatures", 0);
            petition.setProperty("P_Date", new Date());
            datastore.put(petition);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IOException("500p - UNKNOW INTERNAL ERROR");
        }

        return petition;
    }


    //SIGNATURE DES PETITIONS
    @ApiMethod(name = "SignPetition", path="SignPetition/{petId}/{SignerMail}", httpMethod = HttpMethod.PUT)
    public Entity SignPetition(@Named("petId") String petId, @Named("SignerMail") String SignerMail) throws IOException {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key EGPetitions = KeyFactory.createKey("EGPetitions", "EGPetitions");
        Random random=new Random();
        
		List<Entity> listCounter = new ArrayList<>(); 
		for (int i=0;i<20;i++) {
			Entity aCounter = new Entity("Count", i + "_Cntr",EGPetitions);
			listCounter.add(aCounter);
			aCounter.setProperty("val", 0);
			datastore.put(listCounter);
		}
        
        //Key authorKey = KeyFactory.createKey("Author", Long.parseLong(authorId));
        Query.Filter keyFilterS = new FilterPredicate("A_Email", FilterOperator.EQUAL, SignerMail);
        Query SignerQuery = new Query("Author").setFilter(keyFilterS);
        Entity SignerG = datastore.prepare(SignerQuery).asSingleEntity();

        if (SignerG != null) {
            Key petitionKey = KeyFactory.createKey(EGPetitions, "Petition", Long.parseLong(petId));

            ArrayList<Key> petitionsArrayCheck = (ArrayList<Key>) SignerG.getProperty("A_Signed");

            if(!(petitionsArrayCheck.contains(petitionKey))) {

                Query.Filter keyFilterP = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, petitionKey);
                Query petitionQuery = new Query("Petition").setFilter(keyFilterP);
                Entity petitionG = datastore.prepare(petitionQuery).asSingleEntity();

                Transaction txn0=datastore.beginTransaction();
                try{
                    //MAJ DES COUNTER INDEPENDANTS
                    int randomCnt=random.nextInt(listCounter.size());
                    Entity cnt = datastore.get(listCounter.get(randomCnt).getKey());
                    Long cntVal=(Long)cnt.getProperty("val");
                    cnt.setProperty("val", cntVal + 1);
                    datastore.put(cnt);
                    txn0.commit(); 
                }
                catch (Exception e) {
                    e.printStackTrace();
                    txn0.rollback();
                }
                finally
                {
                    if (txn0.isActive()) {
                        txn0.rollback();
                    }
                }

                Transaction txn=datastore.beginTransaction();
                try{
                    //AJOUT DE L'ID DE LA PETITION DANS LES INFORMATIONS DU SIGNATAIRE
                    petitionsArrayCheck.add(petitionG.getKey());
                    SignerG.setProperty("A_Signed", petitionsArrayCheck);
                    datastore.put(txn,SignerG);

                    //INCREMENTATION DU COUNTER GLOBAL SUR LA PETITIONS SIGNE
                    Long mergeCounters=(long) 0;
                    for (Entity cntB : listCounter) {
                        mergeCounters+=(long)datastore.get(cntB.getKey()).getProperty("val");
                    }
    
                    Long petitionSignatureCount = (Long) petitionG.getProperty("P_Signatures");
                    petitionG.setProperty("P_Signatures", petitionSignatureCount + mergeCounters);
                    datastore.put(txn,petitionG);


                    txn.commit(); 
                }
                catch (Exception e) {
                    e.printStackTrace();
                    txn.rollback();
                }
                finally
                {
                    if (txn.isActive()) {
                        txn.rollback();
                    }
                }
    
                return SignerG;
            }
            else {
                throw new IOException("401 - PETITION ALREADY SIGNED");
            }
            
        }
        else {
            throw new IOException("401 - UNAUTHENTIFIED USERS CAN'T SIGN");
        }
        
    }


}
