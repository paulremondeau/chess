interface Semaphore {
    currentRequests: Array<[(value: unknown) => void, (reason?: any) => void, (any: string, foo: string) => Promise<unknown>, [string, string]]>,
    runningRequests: number,
    maxConcurrentRequests: number
}

class Semaphore {
    /**
     * Creates a semaphore that limits the number of concurrent Promises being handled
     * @param {*} maxConcurrentRequests max number of concurrent promises being handled at any time
     */
    constructor(maxConcurrentRequests = 1) {
        this.currentRequests = Array<[(value: unknown) => void, (reason?: any) => void, (any: string, foo: string) => Promise<unknown>, [string, string]]>();
        this.runningRequests = 0;
        this.maxConcurrentRequests = maxConcurrentRequests;
    }

    /**
     * Returns a Promise that will eventually return the result of the function passed in
     * Use this to limit the number of concurrent function executions
     * @param {*} fnToCall function that has a cap on the number of concurrent executions
     * @param  {...any} args any arguments to be passed to fnToCall
     * @returns Promise that will resolve with the resolved value as if the function passed in was directly called
     */
    callFunction(fnToCall: (any: string, foo: string) => Promise<unknown>, ...args: [string, string]) {

        return new Promise((resolve, reject) => {

            this.currentRequests.push([
                resolve,
                reject,
                fnToCall,
                args,
            ]);
            this.tryNext();
        });
    }

    tryNext() {
        if (!this.currentRequests.length) {
            return;
        } else if (this.runningRequests < this.maxConcurrentRequests) {

            let [resolve, reject, fnToCall, args] = this.currentRequests.shift()!;
            this.runningRequests++;
            let req = fnToCall(...args);
            req.then(() => resolve(0)
            ).catch(() => reject(0))
                .finally(() => {
                    this.runningRequests--;
                    this.tryNext();
                    return 0
                });
        }
    }
}

export default Semaphore