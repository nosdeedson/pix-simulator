# Things TODO

## Rules to validate while creating an entryKey.

- try to find an owner with just the key;
    - if one owner exists validate if it has the same taxIdNumber of the request;
    - if yes, same owner:
        - validate account and participant
            - if equal to the request (key already exists) return notification, saying key exists;
            - if not return notification with message suggesting a portability/claim ownership creation, key is in
              another bank (participant);
- if no owner is found with just the key, key does not exist (create key)
    - try to find an owner with taxIdNumber
        - if exists, verify if the accountNumber already exists (use accountNumber and participant)
            - if exists, add to the account the new key
            - if not, create the account
    - if no owner was found create a new Owner.