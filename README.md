# Integration of Slack notfications, AWS SNS, AWS CloudWatch

- This is a deployment package of an AWS lambda function.<br/>
- Slack_NOtification.java is the lambda function which simply forwards the http request to the Slack webhook.


## Steps to use

-Set up a CloudWatch alarm which gets triggered when the given threshold of your favorite metric is met.<br/>
-Create an SNS topic with endpoint as the lambda function.<br/>
-Create a slack webhook and replace it with the current webhook url in the lambda function.<br/>
-Create deployment package, upload and you are all set.<br/>
You would get a slack message when threshold is crossed.<br/>

### Flow of the project

![Flow](flow.png)
