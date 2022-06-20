import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('firebase.json')

firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://vsl-app-443fc-default-rtdb.firebaseio.com/'
})

class User:
    def __init__(self, name, image_url):
        self.name = name
        self.image_url = image_url

    def __repr__(self):
        return f"{self.name}  {self.image_url}"

    def name_to_lower_case(self):
        return self.name.lower()

    def get_image(self):
        return self.image_url

ref = db.reference()
result = ref.get()

# for value in result:
#     print( value)
# print(result[3]['image_url'])

users = []

for value in result:
    user = User(value['Name'], value['image_url']) #convert dictionary to user object
    users.append(user) #adding user object into list

user = users[3] #gettin user object from users list
#print(user.image_url) accesing image url from user object

##print(len(users), user.name_to_lower_case()) #custome method to convert user name to lower case

# for i in range(10):
#     user = users[i]
#     print(i , " = ", user)

#for user in users[0:10]:
#    print( user)

#print(user)
#print(user.image_url)

def retrieve_img():
    return user.get_image()