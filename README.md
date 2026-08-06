# TAAM App - Group 8

This is an artifact management app for the Toronto Asian Art Museum (TAAM). This is a Java Android app that uses XML layouts, Firebase Realtime Database, and cloud image storage (Supabase).

## Members:
- Richard: Scrum Master
- Deon: Developer
- Daniel: Developer
- Jerry: Developer
- Zach: Developer

## Features:

### Login & Signup:
- Users can login with their email and password.
- New users can sign up with their name, email, and password.
- All input fields are validated. e.g Password length >= 6, email must be valid, etc. (Warning via Toast)
- User authentication is handled by Firebase Authentication (Anonymous authentication for guests). Passwords are never stored in RTDB, and are hashed.
- Visitors can create temporary guest accounts to view artifacts. Some functionalities are restricted to users.
- User can switch to the signup page and vice versa.
- Utilizes the MVP code structure.
- `AuthPresenter` unit test provided.

<img width="325" height="650" alt="Screenshot 2026-08-05 235315" src="https://github.com/user-attachments/assets/911c571e-283d-47ab-8d7b-203490ef5606" />
<img width="325" height="650" alt="Screenshot 2026-08-05 235544" src="https://github.com/user-attachments/assets/016ccd19-c83e-480f-8174-bc07174a6e1d" />

### Home Page:
- Home page displays all the artifacts on display.
- Users/Visitors can set the pagination setting to All->12->24 to view a specific amount of artifacts at once.
- Users/Visitors can switch pages if applicable to view the remaining artifacts based on their pagination setting.
- Users/Visitors can search up artifacts to narrow their results.
- Pagination settings are stored locally using `SharedPreferences`
- Users are able to view their saved artifacts. (This button is not visible to guests)
- Users/Visitors can log out of their accounts. Pressing the back button from the home page will automatically log them out.
- Add artifact button are only visible to admins.
- User/Visitors can click on any artifact to open up the expanded view.

<img width="325" height="650" alt="Screenshot 2026-08-05 235727" src="https://github.com/user-attachments/assets/21df9222-3f27-4cdd-ac18-c4491560ef89" />
<img width="325" height="650" alt="Screenshot 2026-08-05 235736" src="https://github.com/user-attachments/assets/137acc12-aa81-4440-a2ff-8b71d97ad4e6" />

<img width="325" height="650" alt="Screenshot 2026-08-06 000300" src="https://github.com/user-attachments/assets/de3ab18c-700f-4145-b938-78f1aad24c16" />
<img width="325" height="650" alt="Screenshot 2026-08-06 000309" src="https://github.com/user-attachments/assets/50e32a01-fdf6-4e14-95c6-b2e34432eb31" />

### Expanded Artifact View:
- Each artifact includes Lot number, artifact name, description, category, material, dynasty/period, etc
- Users can comment on any artifact, and can only delete their own comments.
- Admins can delete any comments (Themselves and others).
- Each expanded view features any related artifacts for the user to browse.
- Users can add the selected artifact to their saved collection.
- Users can like/unlike artifacts (it means they love it).
- Edit/Delete buttons are only visible to the admin.

<img width="325" height="650" alt="Screenshot 2026-08-06 001125" src="https://github.com/user-attachments/assets/abb72289-7a01-4af6-88f2-b244f0235636" />
<img width="325" height="650" alt="Screenshot 2026-08-06 001207" src="https://github.com/user-attachments/assets/1b24f4fd-b765-4ed9-b720-b329138690a2" />

<img width="325" height="650" alt="Screenshot 2026-08-06 001224" src="https://github.com/user-attachments/assets/fa4ded74-053d-4fd9-aaad-2499adbb85e7" />
<img width="325" height="650" alt="Screenshot 2026-08-06 001336" src="https://github.com/user-attachments/assets/76c86d40-a21b-4780-b915-370f54f1f0a7" />


### Adding, Editing, Deleting Artifacts:
- Admins can add new artifacts and specify its fields: Lot number, artifact name, description, category, material, dynasty/period. The rest are optional fields.
- App prevents duplicate lot numbers. They are always unique.
- Admins can delete any artifacts (purpose is to remove old artifacts no longer displayed)
- Admins can edit an existing artifacts.
- Image uploading is handled by Supabase.

<img width="325" height="650" alt="Screenshot 2026-08-06 001843" src="https://github.com/user-attachments/assets/814b4e82-12d2-4568-b70c-8d8af0e6cf3c" />
<img width="325" height="650" alt="Screenshot 2026-08-06 001853" src="https://github.com/user-attachments/assets/4fa15673-c71d-4ce1-9990-ebda75ea176f" />

