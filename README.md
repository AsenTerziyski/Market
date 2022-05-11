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
       
           

Java Interview Task - The Market
Use Spring Boot Framework to build REST API with Gradle/Maven and connect it to a relational DB. We want to see your knowledge about REST, OOP, and SQL skills. 
Please use projections appropriately when interacting with the DB. 
Please avoid query creation from method names so you can demonstrate your SQL skills and ability to write native and JPQL/HQL queries. 
Write a few unit tests. Do not bother with the security or differentiation of users at that point.x
Upload project to GitHub/GitLab.

The Market
The system must operate as a simplified market where users can be buyers or sellers.
Users:
•	user entity attributes: id:1, username:"User1", account:0

The users can buy and sell items.
Items:
•	item entity attributes: id:3, name:Item1, ownerId:1.	
•	example for interacting with items endpoints:
•	create:  {id:1 name:"Item1", ownerId:1};
•	getAllItems with ownerId = 1 (use single query)
[
   {
      "id":3,
      "name":”Item1”,
      "ownerId":1,
      “ownerUsername”:"User1"
   }
]
Example
"User1" owns "Item1". He wants to sell it for $100. He creates an active contract. Other users can review all active contracts and choose to participate. "User2" has enough money in her account and buys "Item1". The contract is now closed. "User1" receives $100 in his account. "User2" is the new owner of "Item1".
Contracts:
•	contract entity attributes: id, sellerId, buyerId, itemId, price,status. (The seller is the owner of the item and can not be the buyer)
•	endpoints - CRUD. Example for interacting with contracts endpoints:
•	create: {itemId : 3, price : 100}. Expected behavior: find the owner of item with id 3 in the DB (ownerId = 1) persist the new active contract in the DB:
  {
      "sellerId":1,
      "itemId":3,
      "price":100,
      "active":true	
   }
	update price of active contract by id: {"itemId":3, "price":200}
•	getAllActive contracts (use single native query):

[
   {
      "sellerId":1,
      “sellerUsername”:"User1",
      "itemId":3,
      "price":200,
      "active":true
   }
]

•	closing active contract by id {"itemId":3, "buyerId":2}. Expected behavior: update the accounts of users with id 1 and id 2. 
•	getAllClosed contracts by optional parameters: itemId, sellerId, buyerId (use single native query):

[
   {
"sellerId":1,
“sellerUsername”:"User1",
"buyerId":2,
“buyerUsername”:"User2",
 "itemId":3,
 "price":100,
 "active":false
   }
]

Bonus
•	getAllItemsByOwnerId (native query), getAllContractsBySellerId (native query)
•	Integrate with 3rd party currency API and persist currency rates. Now the amounts in the user's accounts and prices of items can be in different currencies: USD, EUR, GBP… Example:
•	EUR/USD rate is 1.2 
•	"User1" creates a contract to sell "Item1" for USD 60
•	"User2" has EUR in her account and pays EUR 50 
•	"User1" receives USD 60 in his account.
•	Use Swagger for documentation

