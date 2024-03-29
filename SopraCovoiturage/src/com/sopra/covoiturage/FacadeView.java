package com.sopra.covoiturage;

import java.util.ArrayList;
import java.util.HashMap;

import modele.Information;
import modele.Ride;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import controller.ControllerFacade;

public class FacadeView {
	
	/**
	 * Singleton de la classe FacadeView
	 */
	private static FacadeView singleton ;
	
	private ControllerFacade controller;
	private Activity firstActivity;
	private RideActivity searchRide;
	private WorkplaceManagementActivity workMan;
	private WorkplaceAdditionActivity workAdd;
	private PasswordForgottenActivity pwActivity;
	private RegisterActivity rActivity;
	private UsersActivity uActivity;
	private ProfileModificationActivity modifActivity;
	private String modificationLogin;
	private String profileLogin;
	
	private boolean admin;
	private Information info;
	
	private FacadeView(Activity activity) {
		controller = ControllerFacade.getInstance(this);
		firstActivity = activity;	
	}

	/**
	 * Permet de recuperer l'instance de FacadeView
	 * @return singleton
	 */
	public static FacadeView getInstance (Activity activity) {
		if (singleton == null) {
			singleton = new FacadeView(activity) ;
		}
		return singleton ;
	}
	
	private void setInfo(Information info) {
		this.info = info ;
	}
	
	
	public Information getInfo () {
		return info ;
	}
	
	/**
	 * permet de changer d'activity
	 * @param activity
	 */
	public void changeActivity(Class activity) {
		Intent i = new Intent(firstActivity, activity);
		firstActivity.startActivityForResult(i, 1);
	}

	/**
	 * permet de changer d'activity : WorkplaceAdditionActivity
	 */
	public void changeActivityAddWorkplace() {
		this.workMan.finish();
		Intent i = new Intent(firstActivity, WorkplaceAdditionActivity.class);
		firstActivity.startActivityForResult(i, 1);
	}

	/**
	 * permet de changer d'activity : WorkplaceManagementActivity
	 */
	public void changeActivityManWorkplace() {
		this.workAdd.finish();
		Intent i = new Intent(firstActivity, WorkplaceManagementActivity.class);
		firstActivity.startActivityForResult(i, 1);
	}
	
	/**
	 * changer d'activity a partir de la progressBar
	 * @param activityToFinish
	 * @param newActivity
	 */
	public void changeActivityFromProgressBar (Activity activityToFinish, Class newActivity) {
		activityToFinish.finish();
		changeActivity(newActivity);
	}
	
	/**
	 * lance l'activity ReportActivity
	 * @param activity
	 */
	public void changeActivityReport(Activity activity) {
		Intent i = new Intent(activity, ReportActivity.class);
		activity.startActivityForResult(i, 1);
	}


	public void changeActivityProgressBar(Activity activity, String type, String nickname, String mdp) {
		Intent i = new Intent(activity, ProgressBarActivity.class);
		i.putExtra("type", type) ;
		i.putExtra("nickname", nickname) ;
		i.putExtra("mdp", mdp) ;
		activity.startActivityForResult(i, 1);
	}
	
	/**
	 * se connecter
	 * @param nickname
	 * @param password
	 * @param activity
	 */
	public void performConnect(String nickname, String password, Activity activity) {
		this.setInfo(info) ;
		changeActivityProgressBar(activity, "Connexion en cours...", nickname, password);
	}
		
	/**
	 * se connecter
	 * @param nickname
	 * @param password
	 */
	public void performConnect(String nickname, String password) {
		controller.performConnect(nickname, password) ;
	}

	/**
	 * appelle l'activity d'acceuil lorsqu'un user se connecte
	 * @param admin : boolean, 1 si connection d'un admin
	 */
	public void processConnected(boolean admin) {
		Intent i;
		this.admin = admin;
		if(admin)
			i = new Intent(firstActivity, UsersActivity.class); 
		else 
			i = new Intent(firstActivity, RideActivity.class);
		firstActivity.startActivityForResult(i, 1); 
	}
		

	/**
	 * changer d'activity : profileActivity si user simple, AdminProfileActivity sinon
	 * @param activity
	 */
	public void changeActivityProfile(Activity activity) {
		Intent i;
		if(this.admin) 
			i = new Intent(activity, AdminProfileActivity.class); 
		else 
			i = new Intent(activity, ProfileActivity.class);
			
		activity.startActivityForResult(i, 1); 
	}
	

	/**
	 * Methode pour se deconnecter 
	 */
	public void performDisconnect() {
		controller.performDisconnect();
	}
	
	
	/**
	 * methode permettant d'inscrire un nouvel utilisateur et lance la barre de progression
	 * @param info : informations du profil de l'utilisateur
	 */
	public void performRegister (Information info, Activity activity){
		this.setInfo(info) ;
		changeActivityProgressBar(activity, "Votre inscription est en cours...", "", "");
	}
	
	/**
	 * methode permettant d'inscrire un nouvel utilisateur
	 * @param info : informations du profil de l'utilisateur
	 */
	public void performRegisterGo (Information info){
		controller.performRegister(info);
	}
	
	/**
	 * methode permettant d'envoyer un mail avec un nouveau mot de passe
	 * @param mail
	 */
	public void performPasswordForgotten (String mail) {
		controller.passwordForgotten(mail);
	}
	
	/*PROFILE*/
	
	/**
	 * Methode utilisee seulement par un administrateur
	 * Permet de supprimer un utilisateur
	 * @param nickname : login de l'utilisateur a supprimer
	 * return boolean : 1 si l'utilisateur a ete supprime, 0 sinon
	 */
	public boolean performDeletion (String nickname) {
		return controller.performDeletion(nickname);
	}
	/**
	 * methode permettant de modifier le profil d'un utilisateur
	 * @param info : informations sur l'utilisateur
	 */
	public void performProfileModification (ProfileModificationActivity activity, Information info) {
		this.modifActivity = activity;
		controller.performProfileModification(info);
	}
	
	/**
	 * methode permettant de modifier le profil d'un administrateur
	 * @param info : informations sur l'administrateur
	 */
	public void performAdminProfileModificationRequest (Information info){
		controller.performAdminProfileModificationRequest(info);
	}
	
	/**
	 * Methode permettant de renvoyer les informations sur l'admin ayant pour login nickname 
	 * @param nickname : login de l'admin 
	 * @return Informations : informations sur l'admin 
	 */
	public Information getAdminInformation(String nickname){
		return controller.getAdminInformationRequest(nickname);
	}
	
	/**
	 * Methode permettant de renvoyer les informations sur l'utilisateur ayant pour login nickname 
	 * @param nickname : login de l'utilisateur 
	 * @return Informations : informations sur l'utilisateur 
	 */
	public Information getProfileInformation (String nickname) {
		return controller.getProfileInformation (nickname);
	}
	
	/**
	 * Methode permettant de renvoyer la liste des communes
	 * @return ArrayList<String> : liste des communes
	 */
	public ArrayList<String> getPostcodeList() {
		return controller.getPostcodeList() ;
	}
	
	
	/**
	 * methode appelee si la connection a echoue
	 */
	public void processNotConnected() {
		((ConnectingActivity)firstActivity).notificationConnectionFailure();
	}
	
	public void processNotConnectedNOTTESTED(Activity activity) {
		activity.finish();
		((ConnectingActivity)firstActivity).notificationConnectionFailure();
	}
	
	/**
	 * methode appelee si l'envoi d'un nouveau mot de passe a reussi
	 */
	public void processSendPwdMailOk () {
		if (getPwActivity() == null)
			Log.d("SC","Erreur processSendPwdMailOk");
		else 
			getPwActivity().notificationSendPwdMailOk();		
	}

	/**
	 * methode appelee si l'envoi d'un nouveau mot de passe a echoue
	 */
	public void processSendPwdMailFailure () {
		if (getPwActivity() == null)
			Log.d("SC","Erreur processSendPwdMailFailure");
		else 
			getPwActivity().notificationSendPwdMailFailure();		
	}
	
	/**
	 * notifier l'echec de l'inscription
	 */
	public void notificationRegisterFailure(){
		if (getPwActivity() != null)
			getrActivity().notificationRegisterFailure();	
	}
	
	/**
	 * confirmer la modification d'un profil
	 */
	public void confirmModification () {
		this.modifActivity.finish();
	}
	
	/**
	 * echec de la suppression d'un user
	 */
	public void deletionFailure () {
		UsersActivity uActivity = this.getuActivity();
		if (uActivity != null)
			uActivity.notificationDeletionFailure();
	}
	
	/**
	 * Methode demandant au controller de chercher les trajets partant de postcode pour aller à workplace
	 * @param postcode
	 * @param workplace
	 */
	public void performRides(String postcode, String workplace) {
		this.controller.performRides(postcode, workplace);
	}
	
	/**
	 * Methode initialisation l'attribut ride de l'activite Ride.
	 * @param listOfRides
	 */
	public void processRides (ArrayList<Ride> listOfRides) {
		this.searchRide.setRides(listOfRides);
	}
	

	
	/**
	 * Demande au controller d'ajouter un workplace en base
	 * @param workplace
	 */
	public void addWorkplace(String workplace) {
		this.controller.addWorkplace(workplace);
	}
	
	/**
	 * supprimer un lieu de travail
	 * @param workplace
	 */
	public void deletionWorkplace(String workplace) {
		this.controller.deletionWorkplace(workplace);
	}
	
	
	/**
	 * recuperer tous les utilisateurs et leurs informations
	 * @return ArrayList<Information>
	 */
	public ArrayList<Information> getUsers() {
		return controller.getUsers();
	}
	
	
	/**
	 * Methode permettant renvoyer le number de conducteurs et le nombre de passager
	 * @return String[] requete :
	 * si requete[0] = "-1" : echec et requete[1] = code erreur
	 * sinon requete[0] = nombre total de conducteurs 
	 * 	et requete[1] = nombre total de passagers 
	 */
	public String[] getNumberDriverAndPassenger() {
		String[] res = this.controller.getNumberDriverAndPassenger();
		return res;
	}
	
	/**
	 * Methode permettant de renvoyer le nombre total de connexions
	 * @return String[] requete :
	 * si requete[0] = "0" : succes et requete[1] = nombre total de connexions
	 * sinon requete[0] = "-1" : echec et requete[1] = code erreur 
	 */
	public String[] getNumberConnection() {
		String[] res = this.controller.getNumberConnection();
		return res;
	}
	
	/**
	 * Methode permettant de renvoyer le number de conducteurs et le nombre de passager par trajet
	 * @return HashMap<String,String[]>
	 * Key = String : trajet
	 * Value = String[0] : nombre de conducteurs ; String[1] : nombre de passagers 
	 */
	public HashMap<String,String[]> getNumberDriverAndPassengerPerRide () {
		HashMap<String,String[]> res = this.controller.getNumberDriverAndPassengerPerRide();
		return res;
	}
	
	/**
	 * Methode permettant de renvoyer la liste des lieux de travail
	 * @return ArrayList<String> : liste des lieux de travail
	 */
	public ArrayList<String> getWorkplaces() {
		return controller.getWorkplaces();
	}
	
	/**
	 * Methode permettant de renvoyer le login de l'utilisateur
	 * @return String : login de l'utilisateur
	 */
	public String getLogin() {
		return controller.getLogin();
	}

	/**
	 * Methode permettant de mettre à jour le login
	 */
	public void setLogin(String login) {
		controller.setLogin(login);
	}
	

	/**
	 * Methode permettant de renvoyer les infos correspondantes au user
	 * @return Information : infos correspondantes au user
	 */
	public Information getUserInfo() {
		return controller.getUserInfo();
	}

	/**
	 * Methode permettant de mettre à jour userInfo
	 * @param userInfo
	 */
	public void setUserInfo(Information userInfo) {
		controller.setUserInfo(userInfo);
	}

	/**
	 * methode permettant de mettre a jour RegisterActivity
	 * @param rActivity
	 */
	public void setrActivity(RegisterActivity rActivity) {
		this.rActivity = rActivity;
	}

	/**
	 * methode permettant de recuperer RegisterActivity
	 * @return RegisterActivity
	 */
	public RegisterActivity getrActivity() {
		return rActivity;
	}


	
	/**
	 * methode permettant de recuperer PasswordForgottenActivity
	 * @return PasswordForgottenActivity
	 */
	public PasswordForgottenActivity getPwActivity() {
		return pwActivity;
	}

	/**
	 * Methode permettant de mettre à jour PwActivity
	 * @param pwActivity
	 */
	public void setPwActivity(PasswordForgottenActivity pwActivity) {
		this.pwActivity = pwActivity;
	}

	/**
	 * Methode permettant de mettre à jour searchRide
	 * @param sRide
	 */
	public void setSearchRide(RideActivity sRide) {
		this.searchRide = sRide;
	}	

	/**
	 * Methode permettant de mettre à jour workAdd
	 * @param workAdd
	 */
	public void setWorkAdd(WorkplaceAdditionActivity workAdd) {
		this.workAdd = workAdd;
	}

	/**
	 * Methode permettant de retourner workMan
	 */
	public WorkplaceManagementActivity getWorkMan() {
		return this.workMan;
	}
	
	/**
	 * Methode permettant de mettre à jour workMan
	 * @param workMan
	 */
	public void setWorkMan(WorkplaceManagementActivity workMan) {
		this.workMan = workMan;
	}

	/**
	 * methode permettant de recuperer UsersActivity
	 * @return UsersActivity
	 */
	public UsersActivity getuActivity() {
		return uActivity;
	}

	/**
	 * methode permettant de mettre a jour UsersActivity
	 * @param uActivity
	 */
	public void setuActivity(UsersActivity uActivity) {
		this.uActivity = uActivity;
	}

	/**
	 * Methode permettant de récupérer profileLogin
	 */
	public String getProfileLogin() {
		return profileLogin;
	}

	/**
	 * Methode permettant de mettre à jour profileLogin
	 * @param profileLogin
	 */
	public void setProfileLogin(String profileLogin) {
		this.profileLogin = profileLogin;
	}
	
	/**
	 * methode permettant de recuperer le login modifie
	 * @return string
	 */
	public String getModificationLogin() {
		return modificationLogin;
	}

	/**
	 * methode permettant de mettre a jour le login modifie
	 * @return string
	 */
	public void setModificationLogin(String modificationLogin) {
		this.modificationLogin = modificationLogin;
	}

	public void processUserDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void processUserNotDisconnected() {
		// TODO Auto-generated method stub
		
	}

	public void modificationFailed(int requete) {
		// TODO Auto-generated method stub
		
	}

	public void registrationFailed(int requete) {
		// TODO Auto-generated method stub
		
	}

	public void erreurAddWorkplace() {
		// TODO Auto-generated method stub
		
	}

	public void erreurDeletionWorkplace() {
		// TODO Auto-generated method stub
		
	}

	public void displayTownList(ArrayList<String> townList) {
		// TODO Auto-generated method stub
		
	}

	public void erreurAddTown() {
		// TODO Auto-generated method stub
		
	}
	
}

