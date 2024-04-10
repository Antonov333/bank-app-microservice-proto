## Expense Operation Processing Flow Idea

|                             | Step                                                                                                                    | Notes                                                                                                                          |
|-----------------------------|-------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| **Receiving input data**    | Receiving DTO of Expense operations<br>Storing new Expense Operation entity in Database                                 |                                                                                                                                |
| **Processing**              | Retrieve Currency Exchange Rate<br>Find in DB first, if required data absent refer to external website to retrieve data | Particular entity of Exchange Rate is to be a result of this step. New data retrieved from external is to be saved in database |
|                             | Calculate equivalent USD amount to sum of operation, then keep it and exchangeRateId with Expense Operation entity      |                                                                                                                                |        
|                             | Retrieve expense limit entity applicable to the operation<br/>Keep expensesLimitId with operation entity                |                                                                                                                                |
|                             | Calculate value of Limit Exceeded Flag and set to limitExceeded field of operation entity                               |                                                                                                                                |
| **Saving processed entity** | Save updated entity of Expense Operation in database                                                                    |                                                                                                                                |