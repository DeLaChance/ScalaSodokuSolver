# Scala Sudoku Solver
A small REST application with DB integration written in Scala to do CRUD and solve sudoku grid puzzles.

## Technologies
- JDBC
- PostGres
- Play Framework
- Scala


## Build and run
Install PostGreSQL on your system with default settings (`localhost:5432`).
Add a user `postgres` and password `postgres` and import the `db/sudoku.db` dump file into
your database.

Install `sbt` for your system.

`sbt`

Which will open the SBT shell.

`reload`
`compile`
`run`

This will open the app at port 9000.

## Rest endpoints
- GET `/api/sudoku/`
- GET `/api/sudoku/:id`
- GET `/api/sudoku/new`
- GET `/api/sudoku/:id/solve`
- POST `/api/sudoku/`
- DELETE `/api/sudoku/:id`

Example output GET `/api/sudoku/`:

```
{
    "count": 2,
    "items": [
        {
            "id": "58bde07b-09dd-4287-8268-69bd40436d79",
            "hasError": false,
            "contents": "1,2,3,4,5,6,7,8,9\n4,5,6,7,8,9,1,2,3\n7,8,9,1,2,3,4,5,6\n2,1,4,3,6,5,8,9,7\n3,6,5,8,9,7,2,1,4\n8,9,7,2,1,4,3,6,5\n5,3,1,6,4,2,9,7,8\n6,4,2,9,7,8,5,3,1\n9,7,8,5,3,1,6,4,2"
        },
        {
            "id": "3a170fe0-ebb7-42d6-a07a-d62a74f2da3c",
            "hasError": false,
            "contents": "1,3,4,2,5,6,7,8,9\n2,5,6,7,8,9,1,3,4\n7,8,9,1,3,4,2,5,6\n3,1,2,4,6,5,8,9,7\n4,6,5,8,9,7,3,1,2\n8,9,7,3,1,2,4,6,5\n5,2,1,6,4,3,9,7,8\n6,4,3,9,7,8,5,2,1\n9,7,8,5,2,1,6,4,3"
        }
    ]
}
```

Example input POST `/api/sudoku/`

```
{ 
    "contents": "1,-,-,-,-,-,-,-,-,\n2,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-,\n-,-,-,-,-,-,-,-,-"
}
```
