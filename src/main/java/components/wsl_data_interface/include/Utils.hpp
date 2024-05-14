#ifndef UTILS_HPP
#define UTILS_HPP
#include <iostream>
#include <string>

namespace custom_exception
{
    class ObjConstructedFailure : public std::exception
    {
    private:
        const char *message;

    public:
        ObjConstructedFailure(const char *message) : message(message)
        {
        }

        const char *Msg() { return message; }
    };

    class BindSocketFailure : public std::exception
    {
    private:
        const char *message;

    public:
        BindSocketFailure(const char *message) : message(message)
        {
        }

        const char *Msg() { return message; }
    };
}

#endif