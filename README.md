# NODEOPERATION


This library was designed to operate on a json structure that wants to simulate a filesystem.  
It is part of a larger project, but with small adaptations it can be used by anyone who wants to operate on json nodes.  
 The library uses the Jackson json library; so if you want NodeOperation in the project you have to add the Jackson 2.12 library as well.

author bogojob@gmail.com

version 0.1.0


```JSON
{
	"userid": 0,
	"path": "root",
	"userfilesystem":{
	"nodes": [
		{
		"id": "18cd92",
		"name": "18cd92-directory1",
		"type": "directory",
		"mime": "",
		"size": 0,
		"childs": [],
		"parts": []
	},
	{},
	{}
	,
	.....
]
}
}
```

this type of configuration wants to simulate a file system with which I can perform standard operations such as add, delete or move


## Usage

```java
/*creates a new node that becomes a child of target ("cfd8fc-18cd92")  */
Map<String, String> jsn = new NodeOperation().add("cfd8fc-18cd92",jsondatastring,new NodeOperation().createNode("added node name", "directory", null, 50));

/*the source node (333333-cfd8fc-18cd92) is moved to the destination node (facedd). Node source becomes son of destination */
Map<String, String> jsn = new NodeOperation().move("333333-cfd8fc-18cd92", jsondatastring, "facedd");

/*node source (333333-cfd8fc-18cd92) is removed from the tree*/
Map<String, String> jsn = new  NodeOperation().remove("333333-cfd8fc-18cd92",jsondatastring);
.....
```
**The documentation of the class is in the doc of the repository**

**JUnitTest are present into project**

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
