Social Media REST API
===============================

## Overview

This is a RESTful API that provides backend services for a social media application. It is impelmented using Spring Boot, Jpa, and PostgreSQL. Testing was conducted via collections ran through Postman.

This RESTful API is composed of a series of controllers, which all expose various endpoints that a client may consume. When a compatible request is sent, it is processed in our services layer, then sent to our data layer where our repositories perform their respective CRUD operations on our database. A client may send various requests, such as creating a new user, creating new tweets, liking tweets, and creating/applying hashtags. More details on implementation can be found below.

## Entity Relationship Diagram
![Spring Assessment ERD](https://user-images.githubusercontent.com/12191780/187276918-ccb2d373-be3b-42ff-a74d-5560ba806a10.png)

## Entity Implementations

### User
A user of the application. The `username` must be unique. The `joined` timestamp should be assigned when the user is first created, and must never be updated.
```javascript
{ // User
  username: 'string',
  profile: 'Profile',
  joined: 'timestamp'
}
```

### Profile
A user's profile information. Only the `email` property is required.
```javascript
{ // Profile
  firstName?: 'string',
  lastName?: 'string',
  email: 'string',
  phone?: 'string'
}
```

### Credentials
A user's credentials. These are mostly used for validation and authentication during operations specific to a user. Passwords are plain text for the sake of academic simplicity, this should never be done in an actual business application.
```javascript
{ // Credentials
  username: 'string',
  password: 'string'
}
```

### Hashtag
A hashtag associated with tweets that contain its label. The `label` property must be unique, but is case-insensitive. The `firstUsed` timestamp should be assigned on creation, and must never be updated. The `lastUsed` timestamp should be updated every time a new tweet is tagged with the hashtag.
```javascript
{ // Hashtag
  label: 'string',
  firstUsed: 'timestamp',
  lastUsed: 'timestamp'
}
```

### Tweet
A tweet posted by a user. The `posted` timestamp should be assigned when the tweet is first created, and must not be updated.

There are three distinct variations of tweets: simple, repost, and reply.
- A simple tweet has a `content` value but no `inReplyTo` or `repostOf` values
- A repost has a `repostOf` value but no `content` or `inReplyTo` values
- A reply has `content` and `inReplyTo` values, but no `repostOf` value

```javascript
{ // Tweet
  id: 'integer'
  author: 'User',
  posted: 'timestamp',
  content?: 'string',
  inReplyTo?: 'Tweet',
  repostOf?: 'Tweet'
}
```

### Context
The reply context of a tweet. The `before` property represents the chain of replies that led to the `target` tweet, and the `after` property represents the chain of replies that followed the `target` tweet.

The chains should be in chronological order, and the `after` chain should include all replies of replies, meaning that all branches of replies must be flattened into a single chronological list to fully satisfy the requirements.
```javascript
{ // Context
  target: 'Tweet',
  before: ['Tweet'],
  after: ['Tweet']
}
```

## API Endpoints

### `GET   validate/tag/exists/{label}`
Checks whether or not a given hashtag exists.

#### Response
```javascript
'boolean'
```

### `GET   validate/username/exists/@{username}`
Checks whether or not a given username exists.

#### Response
```javascript
'boolean'
```

### `GET   validate/username/available/@{username}`
Checks whether or not a given username is available.

#### Response
```javascript
'boolean'
```

### `GET     users`
Retrieves all active (non-deleted) users as an array.

#### Response
```javascript
['User']
```

### `POST    users`
Creates a new user. If any required fields are missing or the `username` provided is already taken, an error should be sent in lieu of a response.

If the given credentials match a previously-deleted user, re-activate the deleted user instead of creating a new one.

#### Request
```javascript
{
  credentials: 'Credentials',
  profile: 'Profile'
}
```

#### Response
```javascript
'User'
```

### `GET     users/@{username}`
Retrieves a user with the given username. If no such user exists or is deleted, an error should be sent in lieu of a response.

#### Response
```javascript
'User'
```


### `PATCH   users/@{username}`
Updates the profile of a user with the given username. If no such user exists, the user is deleted, or the provided credentials do not match the user, an error should be sent in lieu of a response. In the case of a successful update, the returned user should contain the updated data.

#### Request
```javascript
{
  credentials: 'Credentials',
  profile: 'Profile'
}
```

#### Response
```javascript
'User'
```

### `DELETE  users/@{username}`
Delete a user with the given username. If no such user exists or the provided credentials do not match the user, an error should be sent in lieu of a response. If a user is successfully deleted, the response should contain the user data prior to deletion. NOTE: This action does not actually drop any records from the database, only soft deletes them.

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'User'
```

### `POST    users/@{username}/follow`
Subscribes the user whose credentials are provided by the request body to the user whose username is given in the url. If there is already a following relationship between the two users, no such followable user exists (deleted or never created), or the credentials provided do not match an active user in the database, an error should be sent as a response. If successful, no data is sent.

#### Request
```javascript
'Credentials'
```

### `POST    users/@{username}/unfollow`
Unsubscribes the user whose credentials are provided by the request body from the user whose username is given in the url. If there is no preexisting following relationship between the two users, no such followable user exists (deleted or never created), or the credentials provided do not match an active user in the database, an error should be sent as a response. If successful, no data is sent.

#### Request
```javascript
'Credentials'
```

### `GET     users/@{username}/feed`
Retrieves all (non-deleted) tweets authored by the user with the given username, as well as all (non-deleted) tweets authored by users the given user is following. This includes simple tweets, reposts, and replies. The tweets should appear in reverse-chronological order. If no active user with that username exists (deleted or never created), an error should be sent in lieu of a response.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/tweets`
Retrieves all (non-deleted) tweets authored by the user with the given username. This includes simple tweets, reposts, and replies. The tweets should appear in reverse-chronological order. If no active user with that username exists (deleted or never created), an error should be sent in lieu of a response.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/mentions`
Retrieves all (non-deleted) tweets in which the user with the given username is mentioned. The tweets should appear in reverse-chronological order. If no active user with that username exists, an error should be sent in lieu of a response.

A user is considered "mentioned" by a tweet if the tweet has `content` and the user's username appears in that content following a `@`.

#### Response
```javascript
['Tweet']
```

### `GET     users/@{username}/followers`
Retrieves the followers of the user with the given username. Only active users should be included in the response. If no active user with the given username exists, an error should be sent in lieu of a response.

#### Response
```javascript
['User']
```

### `GET     users/@{username}/following`
Retrieves the users followed by the user with the given username. Only active users should be included in the response. If no active user with the given username exists, an error should be sent in lieu of a response.

#### Response
```javascript
['User']
```

### `GET     tags`
Retrieves all hashtags tracked by the database.

#### Response
```javascript
['Hashtag']
```

### `GET     tags/{label}`
Retrieves all (non-deleted) tweets tagged with the given hashtag label. The tweets should appear in reverse-chronological order. If no hashtag with the given label exists, an error should be sent in lieu of a response.

A tweet is considered "tagged" by a hashtag if the tweet has `content` and the hashtag's label appears in that content following a `#`

#### Response
```javascript
['Tweet']
```

### `GET     tweets`
Retrieves all (non-deleted) tweets. The tweets should appear in reverse-chronological order.

#### Response
```javascript
['Tweet']
```

### `POST    tweets`
Creates a new simple tweet, with the author set to the user identified by the credentials in the request body. If the given credentials do not match an active user in the database, an error should be sent in lieu of a response.

The response should contain the newly-created tweet.

Because this always creates a simple tweet, it must have a `content` property and may not have `inReplyTo` or `repostOf` properties.

**IMPORTANT:** when a tweet with `content` is created, the server must process the tweet's content for `@{username}` mentions and `#{hashtag}` tags. There is no way to create hashtags or create mentions from the API, so this must be handled automatically!

#### Request
```javascript
{
  content: 'string',
  credentials: 'Credentials'
}
```

#### Response
```javascript
'Tweet'
```

### `GET     tweets/{id}`
Retrieves a tweet with a given id. If no such tweet exists, or the given tweet is deleted, an error should be sent in lieu of a response.

#### Response
```javascript
'Tweet'
```

### `DELETE  tweets/{id}`
Deletes the tweet with the given id. If no such tweet exists or the provided credentials do not match author of the tweet, an error should be sent in lieu of a response. If a tweet is successfully deleted, the response should contain the tweet data prior to deletion. NOTE: This action does not actually drop any records from the database, only soft deletes them.

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'Tweet'
```

### `POST    tweets/{id}/like`
Creates a "like" relationship between the tweet with the given id and the user whose credentials are provided by the request body. If the tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database, an error should be sent. Following successful completion of the operation, no response body is sent.

#### Request
```javascript
'Credentials'
```

### `POST    tweets/{id}/reply`
Creates a reply tweet to the tweet with the given id. The author of the newly-created tweet should match the credentials provided by the request body. If the given tweet is deleted or otherwise doesn't exist, or if the given credentials do not match an active user in the database, an error should be sent in lieu of a response. The response should contain the newly-created tweet.

#### Request
```javascript
{
  content: 'string',
  credentials: 'Credentials'
}
```

#### Response
```javascript
'Tweet'
```

### `POST    tweets/{id}/repost`
Creates a repost of the tweet with the given id. The author of the repost should match the credentials provided in the request body. If the given tweet is deleted or otherwise doesn't exist, or the given credentials do not match an active user in the database, an error should be sent in lieu of a response. The response should contain the newly-created tweet.

#### Request
```javascript
'Credentials'
```

#### Response
```javascript
'Tweet'
```

### `GET     tweets/{id}/tags`
Retrieves the tags associated with the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

#### Response
```javascript
['Hashtag']
```

### `GET     tweets/{id}/likes`
Retrieves the active users who have liked the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted users should be excluded from the response.

#### Response
```javascript
['User']
```

### `GET     tweets/{id}/context`
Retrieves the context of the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

#### Response
```javascript
'Context'
```

### `GET     tweets/{id}/replies`
Retrieves the direct replies to the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted replies to the tweet should be excluded from the response.

#### Response
```javascript
['Tweet']
```

### `GET     tweets/{id}/reposts`
Retrieves the direct reposts of the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted reposts of the tweet should be excluded from the response.

#### Response
```javascript
['Tweet']
```

### `GET     tweets/{id}/mentions`
Retrieves the users mentioned in the tweet with the given id. If that tweet is deleted or otherwise doesn't exist, an error should be sent in lieu of a response.

Deleted users should be excluded from the response.

#### Response
```javascript
['User']
```
