# market

I.	USERS:
1.	Get All Users (GET):
http://localhost:8000/market/users

2.	Register User (POST):
http://localhost:8000/market/users/register
  {
        "username": "User10"
  }
3.	Delete User By Id (DELETE):
http://localhost:8000/market/users/delete/{userId}


II.	ITEMS
1.	Get All Items (GET):
http://localhost:8000/market/items

2.	Get All Items By User Id(GET):
http://localhost:8000/market/items/user/{userId}

3.	Add New Item (POST) – adds new item if user is registered:
http://localhost:8000/market/items/add
{
      "name": "New Item",
      "username": "User1"
}

4.	Edit Item Name (PUT) – edit new item if user is registered:
http://localhost:8000/market/items/edit/{itemId}
    {
        "name": "TestNewName1",
        "username": "User1"
    }
    
5.	Remove item By Id (DELETE):
http://localhost:8000/market/items/remove/{itemId}

6.	Buy Item By Id (POST):
http://localhost:8000/market/items/buy/{itemId}
  {
        "username": "User10"
  }


III.	CONTRACTS
1.	Get All Active Contracts (GET):
http://localhost:8000/market/contracts

2.	Get All Contracts By Seller Id (GET):
http://localhost:8000/market/contracts/seller/{sellerId}

3.	Create Contract (POST):
http://localhost:8000/market/contracts/create
        {
        "username": "User1",
        "itemID": 2,
        "price": 3000
        }
4.	Update Contract (PATCH):
http://localhost:8000/market/contracts/update
       {
       "username": "User1",
       "itemID": 3,
       "price": 3333
       }
       
5.	Delete Contract (DELETE):
http://localhost:8000/market/contracts/delete/{contractId}
       {
       "username": "User1",
       "itemID": 1
       }
